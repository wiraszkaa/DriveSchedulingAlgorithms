public class ElapsedTimeString {

    public static String getTime(long nanoseconds) {
        StringBuilder sb = new StringBuilder();
        if (nanoseconds < 1_000_000) {
            return nanoseconds + "ns";
        }
        if (nanoseconds > 60_000_000_000L) {
            long temp = nanoseconds / 60_000_000_000L;
            nanoseconds -= temp * 60_000_000_000L;
            sb.append(temp).append(" min, ");
        }
        if (nanoseconds > 1_000_000_000) {
            long temp = nanoseconds / 1_000_000_000;
            nanoseconds -= temp * 1_000_000_000;
            sb.append(temp).append(" s, ");
        }
        if (nanoseconds > 1_000_000) {
            long temp = nanoseconds / 1_000_000;
            sb.append(temp).append(" ms");
        }
        return sb.toString();
    }
}
