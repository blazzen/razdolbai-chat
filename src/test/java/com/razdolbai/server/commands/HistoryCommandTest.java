package com.razdolbai.server.commands;

import com.razdolbai.server.Session;
import com.razdolbai.server.history.HistoryAccessObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class HistoryCommandTest {
    private Session mockSession;
    private HistoryAccessObject mockHistoryAccessObject;
    private List<String> testHistory;
    private HistoryCommand testHistoryCommand;
    private String historyContent;
    private String message;
    @Before
    public void setup(){
        mockSession = mock(Session.class);
        mockHistoryAccessObject = mock(HistoryAccessObject.class);
        historyContent = "James has joined the chat";
        message = "Chat history: ";
        testHistory = new ArrayList<String>();
        testHistory.add(historyContent);
        testHistoryCommand = new HistoryCommand(mockSession, mockHistoryAccessObject);
    }
    @Test
    public void shouldExecuteHistoryCommand(){
        when(mockHistoryAccessObject.getHistory()).thenReturn(testHistory);
        testHistoryCommand.execute();
        verify(mockSession).send(message);
        verify(mockHistoryAccessObject).getHistory();
        verify(mockSession).send(historyContent);
    }
}
