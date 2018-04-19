import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * We are as Middle: chain of requests: localhost -> AMS -> box -> AMS -> localhost (Middle)
 */
public class testAMS_Reminder_Delete extends API {

    private API_AMS AMS = new API_AMS();

    @Test
    public void testDelete() throws IOException {
        long reminderScheduleId = 12345;
        long reminderId = 12345;

        ArrayList actual = AMS.Request(mac, Operation.delete, count , reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testDelete_reminderScheduleId_empty__statusCode4() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.delete, count , reminderScheduleId_null, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("4", actual.get(1));
    }

    @Test
    public void testDelete_reminderScheduleId_MAX_VALUE__statusCode4() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.delete, count , Long.MAX_VALUE, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("4", actual.get(1));
    }

    @Test
    public void testDelete_reminderScheduleId_MIN_VALUE() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.delete, count , Long.MIN_VALUE, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: incorrect reminderScheduleId", actual.get(1));
    }

    /** 4 - reminder is unknown. Applies to "Reminders Delete" request (Request ID=1) and "Reminders Modify" request (Request ID=2)
     * @throws IOException - TBD
     */
    @Test
    public void testDelete_reminderId_empty__statusCode4() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.delete, count , reminderScheduleId, 0);
        assertEquals(expected200, actual.get(0));
        assertEquals("4", actual.get(1));
    }

    @Test
    public void testDelete_reminderId_MAX_VALUE__statusCode4() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.delete, count , reminderScheduleId, Long.MAX_VALUE);
        assertEquals(expected200, actual.get(0));
        assertEquals("4", actual.get(1));
    }

    @Test
    public void testDelete_reminderId_MIN_VALUE() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.delete, count , reminderScheduleId, Long.MIN_VALUE);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: incorrect reminderId", actual.get(1));
    }

    /** 4 - reminder is unknown. Applies to "Reminders Delete" request (Request ID=1) and "Reminders Modify" request (Request ID=2)
     * @throws IOException - TBD
     */
    @Test
    public void testDelete_both_empty__statusCode4() throws IOException {
        ArrayList actual = AMS.Request(mac, Operation.delete, count , 0, 0);
        assertEquals(expected200, actual.get(0));
        assertEquals("4", actual.get(1));
    }

    /**
     * @throws IOException - TBD
     */
    @Test
    public void testDelete_mac_empty() throws IOException {
        ArrayList actual = AMS.Request("", Operation.delete, count , reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: wrong deviceId", actual.get(1));
    }

    @Test
    public void testDelete_mac_wrong() throws IOException {
        ArrayList actual = AMS.Request(mac_wrong, Operation.delete, count , reminderScheduleId, reminderId);
        assertEquals(expected500, actual.get(0));
        assertEquals("REM-ST-001 Box is not registered", actual.get(1));
    }

    @Test
    public void testDelete_count_is_0() throws IOException {
        long reminderScheduleId = 2;
        long reminderId = 2;

        ArrayList actual = AMS.Request(mac, Operation.delete, 0, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("REM-008 Reminders parsing error: wrong number of reminders", actual.get(1));
    }

}