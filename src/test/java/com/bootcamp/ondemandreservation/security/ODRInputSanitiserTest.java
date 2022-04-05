package com.bootcamp.ondemandreservation.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ODRInputSanitiserTest {

    @Test
    void basicEmail() {
        assertTrue(ODRInputSanitiser.likelyIsEmail("user@example.com"));
    }
    @Test
    void complexEmail() {
        assertTrue(ODRInputSanitiser.likelyIsEmail("Still.a.Čōяrect.e mail@example.com"));
    }
    @Test
    void complexEmailAlmostNoUser() {
        assertTrue(ODRInputSanitiser.likelyIsEmail("S@example.com"));
    }
    @Test
    void complexEmailAlmostTooLong() {
        assertTrue(ODRInputSanitiser.likelyIsEmail("1234567890@ex.com123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"+
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"+
                "12345678901234567890123456789012345678901234567"));//253 characters
    }
    @Test
    void complexEmailUsernameAlmostTooLong() {
        assertTrue(ODRInputSanitiser.likelyIsEmail("123456789012345678901234567890123456789012345678901234567890123@example.com"));//63 characters before @
    }
    @Test
    void notEmail() {
        assertFalse(ODRInputSanitiser.likelyIsEmail("this is not email"));
    }
    @Test
    void notEmailNull() {
        assertFalse(ODRInputSanitiser.likelyIsEmail(null));
    }
    @Test
    void notEmailEmpty() {
        assertFalse(ODRInputSanitiser.likelyIsEmail(""));
    }
    @Test
    void notEmailNoDomain() {
        assertFalse(ODRInputSanitiser.likelyIsEmail("this is not email@"));
    }
    @Test
    void notEmailNoUser() {
        assertFalse(ODRInputSanitiser.likelyIsEmail("@this.is.not.email"));
    }
    @Test
    void notEmailTooLong() {
        assertFalse(ODRInputSanitiser.likelyIsEmail("1234567890@ex.com123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"+
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"+
                "123456789012345678901234567890123456789012345678"));//254 characters
    }
    @Test
    void notEmailUsernameTooLong() {
        assertFalse(ODRInputSanitiser.likelyIsEmail("1234567890123456789012345678901234567890123456789012345678901234@example.com"));//64 characters before @
    }
    @Test
    void notEmailHackAttempt() {
        assertFalse(ODRInputSanitiser.likelyIsEmail("hello@example.com\u0000 `); DROP TABLE USERS"));
    }
    @Test
    void notEmailBadDomain() {
        assertFalse(ODRInputSanitiser.likelyIsEmail("hello@экзампле.com"));
    }


}