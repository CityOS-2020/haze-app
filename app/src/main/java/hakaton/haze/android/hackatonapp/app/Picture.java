package hakaton.haze.android.hackatonapp.app;

/**
 * Created by mateosokac on 17/04/16.
 */
public class Picture {

    private String name;
    private int drawable;

    public Picture(String name, int drawable){
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

}
