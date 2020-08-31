/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\david\\Google Drive\\School\\CS478\\Project5\\AudioClient\\app\\src\\main\\aidl\\edu\\uic\\cs478\\aidl\\MusicPlayerService.aidl
 */
package edu.uic.cs478.aidl;
// Declare any non-default types here with import statements

public interface MusicPlayerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements edu.uic.cs478.aidl.MusicPlayerService
{
private static final java.lang.String DESCRIPTOR = "edu.uic.cs478.aidl.MusicPlayerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an edu.uic.cs478.aidl.MusicPlayerService interface,
 * generating a proxy if needed.
 */
public static edu.uic.cs478.aidl.MusicPlayerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof edu.uic.cs478.aidl.MusicPlayerService))) {
return ((edu.uic.cs478.aidl.MusicPlayerService)iin);
}
return new edu.uic.cs478.aidl.MusicPlayerService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
java.lang.String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_startSong:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
this.startSong(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_playSong:
{
data.enforceInterface(descriptor);
this.playSong();
reply.writeNoException();
return true;
}
case TRANSACTION_pauseSong:
{
data.enforceInterface(descriptor);
this.pauseSong();
reply.writeNoException();
return true;
}
case TRANSACTION_stopSong:
{
data.enforceInterface(descriptor);
this.stopSong();
reply.writeNoException();
return true;
}
case TRANSACTION_songComplete:
{
data.enforceInterface(descriptor);
boolean _result = this.songComplete();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements edu.uic.cs478.aidl.MusicPlayerService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void startSong(java.lang.String songID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(songID);
mRemote.transact(Stub.TRANSACTION_startSong, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void playSong() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_playSong, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void pauseSong() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pauseSong, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopSong() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopSong, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean songComplete() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_songComplete, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_startSong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_playSong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_pauseSong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_stopSong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_songComplete = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void startSong(java.lang.String songID) throws android.os.RemoteException;
public void playSong() throws android.os.RemoteException;
public void pauseSong() throws android.os.RemoteException;
public void stopSong() throws android.os.RemoteException;
public boolean songComplete() throws android.os.RemoteException;
}
