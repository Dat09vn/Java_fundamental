package com.example;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class AppSessionListener implements HttpSessionListener {

    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int active = ACTIVE_SESSIONS.incrementAndGet();
        System.out.println("[Listener] Session created: " + se.getSession().getId() + " | active sessions: " + active);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int active = ACTIVE_SESSIONS.decrementAndGet();
        System.out.println("[Listener] Session destroyed: " + se.getSession().getId() + " | active sessions: " + active);
    }
}
