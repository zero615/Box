package com.zero.support.box.util;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class Binder {

    private static final Map<Class<?>, BinderCreator<?>> creators = new HashMap<>();
    private static final ParcelCreator.ArrayCreator ARRAY_PARCEL_CREATOR = new ParcelCreator.ArrayCreator();
    private static final ParcelCreator.DefaultCreator DEFAULT_PARCEL_CREATOR = new ParcelCreator.DefaultCreator();
    private static final Map<Type, ParcelCreator> parcelCreators = new HashMap<>();

    private static final BinderCreator<Object> DEFAULT_CREATOR = new BinderCreator<Object>() {
        @Override
        public Object asInterface(IBinder binder, Class<?> cls) {
            return Binder.asInterface(binder, cls);
        }

        @Override
        public IBinder asBinder(Object o, Class<?> cls) {
            return Binder.asBinder(o, cls);
        }
    };


    public static void registerBinderCreator(Class<?> cls, BinderCreator<?> creator) {
        creators.put(cls, creator);
    }

    public static void registerParcelCreator(Class<?> cls, ParcelCreator<?> creator) {
        parcelCreators.put(cls, creator);
    }

    public interface JsonSerializable {
        String toJson() throws Exception;
    }

    static {
        registerParcelCreator(List.class, new ParcelCreator.ListCreator());
    }

    static BinderCreator<?> getBindCreator(Class<?> cls) {
        BinderCreator<?> creator = creators.get(cls);
        if (creator == null) {
            creator = DEFAULT_CREATOR;
        }
        return creator;
    }


    static ParcelCreator getParcelCreator(Type cls) {
        if (cls instanceof Class) {
            if (((Class<?>) cls).isArray()) {
                return ARRAY_PARCEL_CREATOR;
            }
        }
        ParcelCreator creator = parcelCreators.get(cls);
        if (creator == null) {
            creator = DEFAULT_PARCEL_CREATOR;
        }
        return creator;
    }

    public static IBinder peekBinder(Object object) {
        if (object == null) {
            return null;
        }
        return peekBinder(object);
    }

    public static IBinder asBinder(Object object, Class<?> cls) {
        return BinderStub.of(object, cls);
    }

    public static <T> T asInterface(IBinder binder, Class<T> cls) {
        return BinderProxy.asInterface(binder, cls);
    }

    public interface BinderCreator<T> {
        T asInterface(IBinder binder, Class<?> cls);

        IBinder asBinder(T t, Class<?> cls);
    }

    public static class BinderMethod {
        public String name;
        public Method method;
        public Type[] types;
        public Class[] params;

        public Type returnType;
        public Class<?> returnCls;

        public BinderMethod(Method method) {
            this.method = method;
            this.types = method.getGenericParameterTypes();
            this.returnType = method.getGenericReturnType();
            this.name = method.getAnnotation(Name.class).value();
            this.params = method.getParameterTypes();
            this.returnCls = method.getReturnType();
        }
    }

    public static class BinderProxy implements InvocationHandler, IBinder.DeathRecipient {
        private IBinder remote;
        private ClassHolder holder;
        private static final Map<IBinder, Object> localBinders = new HashMap<>();

        public BinderProxy(IBinder remote, Class<?> cls) throws RemoteException {
            this.remote = remote;
            this.holder = new ClassHolder(cls, true);
            String descriptor = remote.getInterfaceDescriptor();
            if (!TextUtils.equals(descriptor, holder.getName())) {
                throw new RemoteException("not exist " + descriptor);
            }
        }


        public static <T> T asInterface(IBinder remote, Class<T> cls) {
            if (remote == null) {
                return null;
            }
            synchronized (localBinders) {
                Object object = localBinders.get(remote);
                if (object == null) {
                    try {
                        object = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new BinderProxy(remote, cls));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return null;
                    }
                    localBinders.put(remote, object);
                }
                return (T) object;
            }
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                BinderMethod binderMethod = holder.getBinderMethod(method.getName());
                data.writeString(binderMethod.name);
                Type[] types = binderMethod.types;
                for (int i = 0; i < types.length; i++) {
                    ParcelCreator creator = getParcelCreator(binderMethod.params[i]);
                    creator.writeToParcel(data, args[i], binderMethod.types[i], binderMethod.params[i]);
                }
                remote.transact(IBinder.FIRST_CALL_TRANSACTION, data, reply, 0);
                reply.readException();
                if (method.getReturnType() != void.class) {
                    ParcelCreator creator = getParcelCreator(binderMethod.returnCls);
                    return creator.readFromParcel(reply, binderMethod.returnType, binderMethod.returnCls);
                }
                return null;
            } catch (Exception Exception) {
                Exception.printStackTrace();
                throw new RemoteException(String.valueOf(Exception));
            } finally {
                data.recycle();
                reply.recycle();
            }

        }

        @Override
        public void binderDied() {
            synchronized (localBinders) {
                localBinders.remove(remote);
            }
        }
    }

    static class BinderStub extends android.os.Binder implements IInterface {
        private final static WeakHashMap<Object, BinderStub> localBinders = new WeakHashMap<>();
        private final Object target;
        private final ClassHolder holder;

        private BinderStub(Object target, Class<?> cls) {
            this.target = target;
            this.holder = new ClassHolder(cls, false);
            attachInterface(this, holder.getName());
        }

        public static android.os.Binder peek(Object object) {
            synchronized (localBinders) {
                return localBinders.get(object);
            }
        }

        public static android.os.Binder of(Object object, Class<?> cls) {
            if (object == null) {
                return null;
            }
            synchronized (localBinders) {
                BinderStub binder = localBinders.get(object);
                if (binder == null) {
                    binder = new BinderStub(object, cls);
                    localBinders.put(object, binder);
                }
                return binder;
            }
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == INTERFACE_TRANSACTION) {
                reply.writeString(holder.getName());
                return true;
            } else if (code != IBinder.FIRST_CALL_TRANSACTION) {
                return false;
            }
            String name = data.readString();
            BinderMethod method = holder.getBinderMethod(name);
            if (method == null) {
                return super.onTransact(code, data, reply, flags);
            }
            try {
                Object[] params = new Object[method.types.length];
                for (int i = 0; i < method.types.length; i++) {
                    ParcelCreator creator = getParcelCreator(method.params[i]);
                    params[i] = creator.readFromParcel(data, method.types[i], method.params[i]);
                }
                Object object = method.method.invoke(target, params);
                reply.writeNoException();
                if (method.returnType != Void.class) {
                    ParcelCreator creator = getParcelCreator(method.returnType);
                    creator.writeToParcel(reply, object, method.returnType, method.returnCls);
                }

            } catch (Exception e) {
                reply.writeException(e);
            }
            return true;
        }


        @Override
        public IBinder asBinder() {
            return this;
        }


    }

    public static class ClassHolder {
        private Map<String, BinderMethod> methods;
        private String name;

        public ClassHolder(Class<?> cls, boolean proxy) {
            Method[] methods = cls.getMethods();
            this.methods = new HashMap<>(methods.length);
            BinderMethod binderMethod;
            this.name = cls.getAnnotation(Name.class).value();
            for (Method method : methods) {
                if (proxy) {
                    this.methods.put(method.getName(), new BinderMethod(method));
                } else {
                    binderMethod = new BinderMethod(method);
                    this.methods.put(binderMethod.name, binderMethod);
                }
            }
        }

        public String getName() {
            return name;
        }

        public BinderMethod getBinderMethod(String name) {
            return methods.get(name);
        }
    }

    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
        String value();
    }

    public static interface ParcelCreator<T> {
        public void writeToParcel(Parcel parcel, T target, Type type, Class<T> rawType) throws Exception;

        T readFromParcel(Parcel parcel, Type type, Class<T> rawType) throws Exception;

        class ListCreator implements ParcelCreator<List> {

            @Override
            public void writeToParcel(Parcel parcel, List target, Type type, Class<List> rawType) throws Exception {
                ParameterizedType parameterizedType = (ParameterizedType) type;

                Type[] types = parameterizedType.getActualTypeArguments();
                if (types.length == 0) {
                    parcel.writeList(target);
                    return;
                }
                if (target == null) {
                    parcel.writeInt(-1);
                    return;
                }
                ParcelCreator creator = Binder.getParcelCreator(types[0]);
                if (creator == null) {
                    throw new RuntimeException("not found creator for " + types[0]);
                }
                for (Object o : target) {
                    creator.writeToParcel(parcel, o, types[0], (Class<?>) types[0]);
                }
            }

            @Override
            public List readFromParcel(Parcel parcel, Type type, Class<List> rawType) throws Exception {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] types = parameterizedType.getActualTypeArguments();
                if (types.length == 0) {
                    return parcel.readArrayList(getClass().getClassLoader());
                }

                int N = parcel.readInt();
                if (N == -1) {
                    return null;
                }
                List list = new ArrayList();
                ParcelCreator creator = Binder.getParcelCreator(types[0]);
                if (creator == null) {
                    throw new RuntimeException("not found creator for " + types[0]);
                }
                for (int i = 0; i < N; i++) {
                    list.add(creator.readFromParcel(parcel, types[0], (Class<?>) types[0]));
                }
                return list;
            }
        }

        class ArrayCreator implements ParcelCreator<Object[]> {

            @Override
            public void writeToParcel(Parcel parcel, Object[] object, Type type, Class<Object[]> rawType) throws Exception {
                Object[] target = (Object[]) object;
                if (object == null) {
                    parcel.writeInt(-1);
                    return;
                }
                parcel.writeInt(target.length);
                Class<?> subType = rawType.getComponentType();
                ParcelCreator creator = Binder.getParcelCreator(subType);
                if (creator == null) {
                    throw new RuntimeException("not found creator for " + subType);
                }
                for (Object o : target) {
                    creator.writeToParcel(parcel, o, subType, subType);
                }
            }

            @Override
            public Object[] readFromParcel(Parcel parcel, Type type, Class<Object[]> rawType) throws Exception {
                int N = parcel.readInt();
                if (N == -1) {
                    return null;
                }
                Class<?> subType = rawType.getComponentType();
                if (subType == null) {
                    return null;
                }
                Object[] list = (Object[]) Array.newInstance(subType, N);

                ParcelCreator creator = Binder.getParcelCreator(subType);
                if (creator == null) {
                    throw new RuntimeException("not found creator for " + subType);
                }
                for (int i = 0; i < N; i++) {
                    list[i] = (creator.readFromParcel(parcel, subType, subType));
                }
                return list;
            }
        }

        public class DefaultCreator implements ParcelCreator<Object> {

            @Override
            public void writeToParcel(Parcel reply, Object object, Type type, Class rawType) throws Exception {
                if (rawType.isInterface()) {
                    if (object == null) {
                        reply.writeStrongBinder(null);
                    } else {
                        BinderCreator creator = Binder.getBindCreator(rawType);
                        reply.writeStrongBinder(creator.asBinder(object, rawType));
                    }
                } else if (rawType.isPrimitive()) {
                    if (rawType == int.class) {
                        reply.writeInt((Integer) object);
                    } else if (rawType == float.class) {
                        reply.writeFloat((float) object);
                    } else if (rawType == double.class) {
                        reply.writeDouble((double) object);
                    } else if (rawType == boolean.class) {
                        reply.writeInt(((boolean) object) ? 1 : 0);
                    } else if (rawType == byte.class) {
                        reply.writeByte((byte) object);
                    } else if (rawType == long.class) {
                        reply.writeLong((long) object);
                    }
                } else if (rawType == String.class) {
                    reply.writeString((String) object);
                } else if (List.class.isAssignableFrom(rawType)) {
                    reply.writeList((List) object);
                } else if (Bundle.class.isAssignableFrom(rawType)) {
                    reply.writeBundle((Bundle) object);
                } else if (JsonSerializable.class.isAssignableFrom(rawType)) {
                    if (object == null) {
                        reply.writeString(null);
                    } else {
                        reply.writeString(((JsonSerializable) object).toJson());
                    }
                } else if (Parcelable.class.isAssignableFrom(rawType)) {
                    reply.writeParcelable((Parcelable) object, 0);
                } else if (rawType == (IBinder.class)) {
                    reply.writeStrongBinder((IBinder) object);
                }
            }

            @Override
            public Object readFromParcel(Parcel data, Type type, Class rawType) throws Exception {
                if (rawType == int.class) {
                    return data.readInt();
                } else if (rawType == float.class) {
                    return data.readFloat();
                } else if (rawType == double.class) {
                    return data.readDouble();
                } else if (rawType == boolean.class) {
                    return data.readInt() != 0;
                } else if (rawType == byte.class) {
                    return data.readByte();
                } else if (rawType == long.class) {
                    return data.readLong();
                } else if (rawType == short.class) {
                    return (short) data.readInt();
                } else if (rawType == String.class) {
                    return data.readString();
                } else if (List.class.isAssignableFrom(rawType)) {
                    return data.readArrayList(Binder.class.getClassLoader());
                } else if (Bundle.class.isAssignableFrom(rawType)) {
                    return data.readBundle(rawType.getClassLoader());
                } else if (JsonSerializable.class.isAssignableFrom(rawType)) {
                    String json = data.readString();
                    if (json == null) {
                        return null;
                    } else {
                        return rawType.getConstructor(String.class).newInstance(json);
                    }
                } else if (Parcelable.class.isAssignableFrom(rawType)) {
                    return data.readParcelable(rawType.getClassLoader());
                } else if (rawType == IBinder.class) {
                    return data.readStrongBinder();
                } else {
                    IBinder binder = data.readStrongBinder();
                    if (binder == null) {
                        return null;
                    }
                    BinderCreator<?> creator = Binder.getBindCreator(rawType);
                    return creator.asInterface(binder, rawType);
                }
            }
        }
    }

}
