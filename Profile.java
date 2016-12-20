package socialnet;

/**
 * Created by preston.peterson651 on 12/2/15.
 */
public class Profile implements Comparable<Profile> {

    private String name;
    private String phoneNumber;
    private String cityOfResidence;

    Profile(String name, String phoneNumber, String cityOfResidence) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cityOfResidence = cityOfResidence;
    }

    public String getName() {
        return this.name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCityOfResidence(String cityOfResidence) {
        this.cityOfResidence = cityOfResidence;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getCityOfResidence() {
        return this.cityOfResidence;
    }


    /**
     * @param other
     * @return 1 if name is unique or 0 if name is a duplicate entry
     */
    @Override
    public int compareTo(Profile other) {
        return this.name.compareToIgnoreCase(other.getName());
    }
}
