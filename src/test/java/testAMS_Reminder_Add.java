import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * We are as Middle: chain of requests: localhost -> AMS -> box -> AMS -> localhost (Middle)
 */
class testAMS_Reminder_Add extends API {
    //"reminderChannelNumber": <new value for the DCN the reminder is set to>,
    //"reminderProgramStart": "<new value for the date/time of program the reminder is set to>",
    //"reminderProgramId": "<new value for the TMS Program ID the reminder is set to>",
    //"reminderOffset": <new value for the number of minutes before the program start the reminder should be shown>
    //"reminderScheduleId": "<series or Individual program reminder schedule reference ID>",
    //"reminderId": "<episode or Individual program reminder reference ID of a particular schedule>",

    private API_AMS AMS = new API_AMS();

    /** common test for Adding
     * @throws IOException
     */
    @Test
    void testAdd() throws IOException {
        //int count = 500;
        //int reminderChannelNumber = 305;
        //long reminderScheduleId = 1;
        //long reminderId = 1;
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    /** 3 - reminder is set for unknown channel. \"Reminders Add\" request (Request ID=0)
     * @throws IOException
     */
    @Test
    void testAdd_reminderChannelNumber_empty__statusCode3() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber_empty, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("3", actual.get(1));
    }

    @RepeatedTest(10)
    void testAdd_reminderChannelNumber_MAX_VALUE__statusCode3() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), Integer.MAX_VALUE, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("3", actual.get(1));
    }

    @Test
    void testAdd_reminderChannelNumber_MIN_VALUE() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), Integer.MIN_VALUE, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: invalid channel number", actual.get(1));
    }

    @Test
    void testAdd_reminderProgramStart_empty() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, "", reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: missing program start", actual.get(1));
    }

    /** 2 - reminder is set for time in the past. Applies to "Reminders Add" request (Request ID=0)
     * @throws IOException - TBD
     */
    @Test
    void testAdd_reminderProgramStart_PAST__statusCode2() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, "2000-01-01", reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("2", actual.get(1));
    }

    @Test
    void testAdd_reminderProgramStart_wrong() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart_wrong, reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: invalid program start", actual.get(1));
    }

    @Test
    void testAdd_reminderProgramStart_text() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart_text, reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected500, actual.get(0));
        assertEquals("name cannot be null", actual.get(1));
    }

    /** reminderProgramId is optional and AMS is removed his from json
     * @throws IOException
     */
    @Test
     void testAdd_reminderProgramId_empty() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, "", reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
        //assertEquals("REM-008 Reminders parsing error: wrong programId", actual.get(1));
    }

    /** reminderProgramId is optional and AMS is removed his from json
     * @throws IOException
     */
    @Test
    void testAdd_reminderProgramId_wrong() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, "EP#@$%#$%@#$^$#%^#$%^", reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
        //assertEquals("REM-008 Reminders parsing error: wrong programId", actual.get(1));
    }

    @Test
    void testAdd_reminderOffset_empty() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, -1, reminderScheduleId, reminderId);
        assertEquals(expected500, actual.get(0));
        assertEquals("name cannot be null", actual.get(1));
    }

    @Test
    void testAdd_reminderOffset_wrong() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, -2, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: missing offset", actual.get(1));
    }

    @RepeatedTest(10)
    void testAdd_reminderOffset_MAX_VALUE() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, Integer.MAX_VALUE, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @RepeatedTest(1)
    void testAdd_reminderOffset_MIN_VALUE() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, Integer.MIN_VALUE, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: invalid offset", actual.get(1));
    }

    @RepeatedTest(100)
    //todo https://jira.zodiac.tv/browse/CH0098-169
    void testAdd_reminderScheduleId_zero() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, 0, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: incorrect ScheduleId", actual.get(1));
        //assertEquals(expected200, actual.get(0));
        //assertEquals("4", actual.get(1));
    }

    @RepeatedTest(1)
    void testAdd_reminderScheduleId_empty() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, -1, reminderId);
        assertEquals(expected500, actual.get(0));
        assertEquals("name cannot be null", actual.get(1));
    }

    @RepeatedTest(10)
    //todo https://jira.zodiac.tv/browse/CH0098-169
    void testAdd_reminderScheduleId_zero_reminderId_zero() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, 5, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, 0, 0);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: incorrect ScheduleId", actual.get(1));
        //assertEquals(expected200, actual.get(0));
        //assertEquals("4", actual.get(1));
    }

    @RepeatedTest(10)
    void testAdd_reminderScheduleId_MAX_VALUE() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, Long.MAX_VALUE, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @RepeatedTest(1)
    void testAdd_reminderScheduleId_MIN_VALUE() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, Long.MIN_VALUE, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: incorrect reminderScheduleId", actual.get(1));
    }

    @RepeatedTest(100)
        //todo https://jira.zodiac.tv/browse/CH0098-169
    void testAdd_reminderId_zero() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, 1, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, 0);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: incorrect reminderId", actual.get(1));
        //assertEquals(expected200, actual.get(0));
        //assertEquals("4", actual.get(1));
    }

    @RepeatedTest(1)
    void testAdd_reminderId_empty() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, 2, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, -1);
        assertEquals(expected500, actual.get(0));
        assertEquals("name cannot be null", actual.get(1));
    }

    @RepeatedTest(10)
    void testAdd_reminderId_MAX_VALUE() throws IOException {
        //long max_value = 18446744073709551615L;
        //9223372036854775807L
        ArrayList actual = AMS.Request(mac, Operation.add, 1, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, Long.MAX_VALUE);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @RepeatedTest(10)
    void testAdd_reminderId_MIN_VALUE() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, Long.MIN_VALUE);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: incorrect reminderId", actual.get(1));
    }

    /**5 - reminder with provided pair of identifiers (reminderScheduleId and reminderId) is already set (for Add Reminder request)
     * @throws IOException - TBD
     */
    @RepeatedTest(10)
    void testAdd_statusCode5() throws IOException {
        //todo
        //todo count=1 MUST BE !!! otherwise random will be used
        int count = 1;
        long reminderScheduleId = reminderScheduleId();
        long reminderId = reminderId();

        ArrayList actual;
        actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));

        actual = AMS.Request(mac, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("5", actual.get(1));
    }

    @Test
    void testAdd_mac_empty() throws IOException {
        ArrayList actual = AMS.Request("", Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: wrong deviceId", actual.get(1));
    }

    @Test
    void testAdd_mac_wrong() throws IOException {
        ArrayList actual = AMS.Request(mac_wrong, Operation.add, count, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("REM-ST-001 Box is not registered", actual.get(1));
    }

    @Test
    void testAdd_count_is_0() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.add, 0, reminderProgramStart(), reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: wrong number of reminders", actual.get(1));
    }

    @Disabled
    void testPurge() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.purge);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }
}