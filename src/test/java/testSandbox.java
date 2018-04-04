import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class testSandbox extends API {

    private API_AMS AMS = new API_AMS();

    @Test
    public void testDate() {
        assertEquals("2018-03-31", reminderProgramStart());
        assertEquals("2018-03-31", get_date(1, false));
        assertEquals("2018-03-31 2018-04-01", get_date(2, true));
        assertEquals("2018-04-01", get_date(2, false));
    }

    @Test
    public void testTime() {
        assertEquals("00:30", get_time(1, 2));
    }

    @Test
    public void testOperation_NewAPI_400_Bad_Request() throws IOException {
        ArrayList actual = AMS.Request(ams_ip, macaddress, Operation.blablabla, count_reminders,
                reminderProgramStart(), reminderChannelNumber, reminderProgramId,
                reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testOracleDB_Query() throws SQLException, ClassNotFoundException {
        ArrayList actual = AMS.QueryDB(ams_ip, macaddress);
        assertFalse(actual.isEmpty());

        assertEquals(Long.class, actual.get(0).getClass());
        assertNotEquals(0, actual.get(0));

        assertEquals(Long.class, actual.get(1).getClass());
        assertNotEquals(0, actual.get(1));

        assertEquals(Integer.class, actual.get(2).getClass());
        assertEquals(0, actual.get(2));

        assertEquals(String.class, actual.get(3).getClass());
        assertNotEquals(0, actual.get(3));

        assertEquals(String.class, actual.get(4).getClass());
        assertNotEquals(0, actual.get(4));
    }

    @Test
    public void testOracleDB_Query_macaddress_empty() throws SQLException, ClassNotFoundException {
        ArrayList result = AMS.QueryDB(ams_ip, "");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testOracleDB_Query_macaddress_wrong() throws SQLException, ClassNotFoundException {
        ArrayList result = AMS.QueryDB(ams_ip, macaddress_wrong);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheck_Delete() throws IOException {
        ArrayList actual = AMS.Request(ams_ip, macaddress, Operation.delete, count_reminders,
                reminderProgramStart(), reminderChannelNumber,
                reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

}
