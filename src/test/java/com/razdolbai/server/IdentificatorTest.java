package com.razdolbai.server;

import com.razdolbai.server.exceptions.OccupiedNicknameException;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class IdentificatorTest {
    private Identificator identificator = new Identificator();

    @Test
    public void shouldAddNickNameWhenNickNameReceivedAndOldIsNull() throws OccupiedNicknameException {
        String oldNick = null;
        String newNick = "newNick";
        identificator.changeNickname(oldNick, newNick);
        Set<String> nicknames = (Set<String>) Whitebox.getInternalState(identificator, "nicknames");
        assertEquals(nicknames.size(), 1);
        assertTrue(nicknames.contains(newNick));
        assertFalse(nicknames.contains(oldNick));
    }

    @Test
    public void shouldAddNewNickNameAndRemoveOldWhenNickNameChanged() throws OccupiedNicknameException {
        String oldNick = "oldNick";
        String newNick = "newNick";
        identificator.changeNickname(oldNick, newNick);
        Set<String> nicknames = (Set<String>) Whitebox.getInternalState(identificator, "nicknames");
        assertEquals(nicknames.size(), 1);
        assertTrue(nicknames.contains(newNick));
        assertFalse(nicknames.contains(oldNick));
    }

    @Test(expected = OccupiedNicknameException.class)
    public void shouldThrowsOccupiedNicknameExceptionWhenNickNameIsAlreadyInSet() throws OccupiedNicknameException {
        String oldNick = "oldNick";
        String newNick = "newNick";
        HashSet<String> nicknamesField = new HashSet<>();
        nicknamesField.add(newNick);
        Whitebox.setInternalState(identificator, "nicknames", nicknamesField);

        identificator.changeNickname(oldNick, newNick);
        Set<String> nicknames = (Set<String>) Whitebox.getInternalState(identificator, "nicknames");
        assertEquals(nicknames.size(), 1);
        assertTrue(nicknames.contains(newNick));
    }
}
