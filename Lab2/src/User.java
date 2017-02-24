import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Baheer.
 * <p>
 * This class is user to save the key stroke and calculate deviation from the sets of key stroke
 */
public class User {
//First Key Code	Second Key Code		Fly Time		First Key Dwell Time		Second Key Dwel Time

    ArrayList<KeyStroke> keyStrokes;

    /*No one is allow to call this outside of the class**/
    private User() {
        keyStrokes = new ArrayList<>();
    }

    /**
     * Load in the user from an file
     */
    public static User loadUser(String file) throws Exception {
        User u = new User();
        BufferedReader in = new BufferedReader(new FileReader(new File(file)));
        String line;
        in.readLine(); //just skip the first line
        while ((line = in.readLine()) != null) {
            while (line.contains("\t\t"))
                line = line.replaceAll("\t\t", "\t");
            String[] items = line.split("\t");
            u.addFile(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2])
                    , Integer.parseInt(items[3]), Integer.parseInt(items[4]));
        }
        return u;
    }

    /**
     * calculate the Deviation and return it
     *
     * @param vset The frist set
     * @param tset The second set
     **/
    private static double calculateDeviation(KeyStroke[] vset, KeyStroke[] tset) {
        double digraphSum = 0, monographSum = 0;
        for (int i = 0; i < vset.length; i++) {
            if (tset[i].flyTime != 0)
                digraphSum += Math.abs(vset[i].flyTime - tset[i].flyTime) / (double) tset[i].flyTime;
            if (tset[i].secondKeyDwell != 0 && i < vset.length - 1)
                monographSum += Math.abs(vset[i].firstKeyDwell - tset[i].firstKeyDwell) / (double) tset[i].firstKeyDwell;
        }
        return 50 * ((digraphSum / (vset.length - 1)) + (monographSum / vset.length));
    }

    /**
     * @param loginUser   The user who is trying to login
     * @param others      The other users to check against
     * @param numberOfSet The number of set the list of keystoke is spilt into
     * @param threshold   The limit before it is accept
     **/
    public static double calculateFRR(User loginUser, User[] others, int numberOfSet, int threshold) {
        double total = 0;
        double FRR = 0;
        for (User user : others) {
            double[] Ud = User.calculateDeviation(loginUser, user, numberOfSet);
            for (double d : Ud) {
                total++;
                if (d > threshold)
                    FRR++;
            }

        }
        return FRR / total;
    }

    /**
     * @param user        The user who is trying to login
     * @param numberOfSet The number of set the list of keystoke is spilt into
     * @param threshold   The limit before it is accept
     */
    public static double calculateFAR(User user, int numberOfSet, int threshold) {
        double[] Ud = User.calculateDeviation(user, numberOfSet);
        double rf = 0;
        for (double d : Ud) {
            if (d > threshold)
                rf++;
        }

        return rf / Ud.length;
    }


    private static double[] calculateDeviation(User user, int numberOfSet) {
        return calculateDeviation(user, user, numberOfSet);
    }

    private static double[] calculateDeviation(User user, User other, int numberOfSet) {
        double[] deviation = new double[numberOfSet - 1];
        int setSize = user.getKeyStrokes().size() / numberOfSet;
        KeyStroke[] base = new KeyStroke[setSize];

        System.arraycopy(user.getKeyStrokes().toArray(), 0, base, 0, setSize);
        KeyStroke[] keysSet = new KeyStroke[setSize];

        for (int i = 1; i < numberOfSet; i++) {
            System.arraycopy(other.getKeyStrokes().toArray(), setSize * i, keysSet, 0, setSize);
            deviation[i - 1] = calculateDeviation(base, keysSet);
        }

        return deviation;
    }

    private void addFile(int firstKey, int secondKey, int flyTime, int firstKeyDwell, int secondKeyDwell) {
        keyStrokes.add(new KeyStroke(firstKey, secondKey, flyTime, firstKeyDwell, secondKeyDwell));
    }

    public ArrayList<KeyStroke> getKeyStrokes() {
        return keyStrokes;
    }

    /**
     * This class hold the keystroke of the user
     */
    private class KeyStroke {

        int firstKey;
        int secondKey;
        int flyTime;
        int firstKeyDwell;
        int secondKeyDwell;

        public KeyStroke(int firstKey, int secondKey, int flyTime, int firstKeyDwell, int secondKeyDwell) {
            this.firstKey = firstKey;
            this.secondKey = secondKey;
            this.flyTime = flyTime;
            this.firstKeyDwell = firstKeyDwell;
            this.secondKeyDwell = secondKeyDwell;
        }

        @Override
        public String toString() {
            return "KeyStroke{" +
                    "firstKey=" + firstKey +
                    ", secondKey=" + secondKey +
                    ", flyTime=" + flyTime +
                    ", firstKeyDwell=" + firstKeyDwell +
                    ", secondKeyDwell=" + secondKeyDwell +
                    '}';
        }
    }

}
