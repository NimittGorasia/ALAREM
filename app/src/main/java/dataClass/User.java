package dataClass;
import java.util.*;

public class User {
    public String email;
    public List<String> waktu;
    public Float langitude;
    public Float latitude;

    public User() {
        waktu = new ArrayList<>();
    }

    public User(String email) {
        this.email = email;
        this.waktu = new ArrayList<>();
        this.waktu.add("06:30");
    }

    public User(String email, Float langitude, Float latitude) {
        this.email = email;
        this.waktu = new ArrayList<>();
        this.waktu.add("06:30");
        this.langitude = langitude;
        this.latitude = latitude;
    }

    public User(String email, ArrayList<String> waktu) {
        this.email = email;

        waktu = new ArrayList<>();
        Collections.copy(this.waktu, waktu);
    }

    public List<String> getWaktu() {
        return this.waktu;
    }
}
