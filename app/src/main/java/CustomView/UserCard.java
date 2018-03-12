package CustomView;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import Model.RequestResponseTypes.User;
import gabrieltechnologies.sehm.R;

public class UserCard extends RelativeLayout{
    private TextView username;
    private TextView email;
    private ImageView thumbnail;
    private Button addFriendBtn;

    User user;

    public UserCard(Context context) {
        super(context);
        init();
    }

    public UserCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.user_card, this);
        this.username = findViewById(R.id.username);
        this.email = findViewById(R.id.email);
        this.thumbnail = findViewById(R.id.thumbnail);
        this.addFriendBtn = findViewById(R.id.add_friend);
    }

    public void setUser(final User user, final ICardCallback cardCallback, Activity activity) {
        this.user = user;


        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                username.setText(user.getUsername());
                email.setText(user.getEmail());
                Picasso.with(getContext()).load(user.getPicture()).into(thumbnail);
            }
        });


        this.addFriendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cardCallback.addFriendPressed(user.getGoogleId());
            }
        });
    }
}
