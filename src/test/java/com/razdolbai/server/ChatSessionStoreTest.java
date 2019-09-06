package com.razdolbai.server;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ChatSessionStoreTest {
    private ReadWriteLock rwlMock;
    private ReentrantReadWriteLock.ReadLock rlMock;
    private ReentrantReadWriteLock.WriteLock wlMock;
    private Collection<Session> sessions;
    private ExecutorService executorServiceMock;
    private ChatSessionStore sessionStore;

    @Before
    public void setUp() {
        rwlMock = mock(ReentrantReadWriteLock.class);
        rlMock = mock(ReentrantReadWriteLock.ReadLock.class);
        wlMock = mock(ReentrantReadWriteLock.WriteLock.class);
        executorServiceMock = mock(ExecutorService.class);
        sessions = new HashSet<>();
        when(rwlMock.readLock()).thenReturn(rlMock);
        when(rwlMock.writeLock()).thenReturn(wlMock);
        doNothing().when(executorServiceMock).execute(any(Runnable.class));
        doNothing().when(executorServiceMock).shutdown();

        sessionStore = new ChatSessionStore();
        Whitebox.setInternalState(sessionStore, "rwl", rwlMock);
        Whitebox.setInternalState(sessionStore, "sessions", sessions);
        Whitebox.setInternalState(sessionStore, "executorService", executorServiceMock);
    }

    @Test
    public void testSessionAddedWhenRegister() {
        Session sessionMock = mock(Session.class);
        sessionStore.register(sessionMock);
        verify(rlMock, never()).lock();
        verify(rlMock, never()).unlock();
        verify(wlMock).lock();
        verify(wlMock).unlock();
        verify(executorServiceMock).execute(any(Runnable.class));
        assertEquals(sessions.size(), 1);
    }

    @Test(expected = Exception.class)
    public void testSessionStoreWriteLockUnlockedWhenExceptionIsThrownInRegister() {
        Session sessionMock = mock(Session.class);
        doThrow(Exception.class).when(wlMock).lock();
        sessionStore.register(sessionMock);
        verify(rlMock, never()).lock();
        verify(rlMock, never()).unlock();
        verify(wlMock).lock();
        verify(wlMock).unlock();
        verify(executorServiceMock, never()).execute(any(Runnable.class));
        assertEquals(sessions.size(), 1);
    }

    @Test
    public void testEachSessionSendCalledWhenSendToAll() {
        Session firstSessionMock = mock(Session.class);
        Session secondSessionMock = mock(Session.class);
        String message = "message";
        doNothing().when(firstSessionMock).send(message);
        doNothing().when(secondSessionMock).send(message);
        sessions.add(firstSessionMock);
        sessions.add(secondSessionMock);
        sessionStore.sendToAll(message);
        verify(rlMock).lock();
        verify(rlMock).unlock();
        verify(wlMock, never()).lock();
        verify(wlMock, never()).unlock();
        verify(firstSessionMock).send(message);
        verify(secondSessionMock).send(message);
    }

    @Test
    public void testEachSessionCloseCalledWhenCloseAll() {
        Session firstSessionMock = mock(Session.class);
        Session secondSessionMock = mock(Session.class);
        doNothing().when(firstSessionMock).close();
        doNothing().when(secondSessionMock).close();
        sessions.add(firstSessionMock);
        sessions.add(secondSessionMock);
        sessionStore.closeAll();
        verify(rlMock).lock();
        verify(rlMock).unlock();
        verify(wlMock, never()).lock();
        verify(wlMock, never()).unlock();
        verify(firstSessionMock).close();
        verify(secondSessionMock).close();
        verify(executorServiceMock).shutdown();
    }

    @Test(expected = Exception.class)
    public void testSessionStoreWriteLockUnlockedWhenExceptionIsThrownInCloseALl() {
        doThrow(Exception.class).when(rlMock).lock();
        sessionStore.closeAll();
        verify(rlMock).lock();
        verify(rlMock).unlock();
        verify(wlMock, never()).lock();
        verify(wlMock, never()).unlock();
        verify(executorServiceMock, never()).shutdown();
        verify(sessions, never()).forEach(any());
    }
}
