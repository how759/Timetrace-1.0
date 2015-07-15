package com.timetrace.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity
        implements OnClickListener, OnCheckedChangeListener {
    private static final String TAG = "GONJI MainActivity";
    private Button dowbelevator_btn;
    private Button downstair_btn;
    private Button drivecar_btn;
    private boolean initialization = false;
    private Button inpocket_btn;
    private Intent intent = null;
    private boolean isJacket = false;
    private boolean isLeft = true;
    private boolean isRight = false;
    private Button jumping_btn;
    private Button lying_btn;
    private boolean mode1run = true;
    private boolean mode2run = true;
    private RadioGroup mode_selector1;
    private RadioGroup mode_selector2;
    private EditText name_edt;
    private String position = "NoPosition";
    private Button ridebike_btn;
    private Button ridebus_btn;
    private Button running_btn;
    private Button sitting_btn;
    private Button standing_btn;
    private String state = null;
    private String sub_name = "SubjectA";
    private Button tv_btn;
    private Button upelevator_btn;
    private Button upstair_btn;
    private Button walking_btn;

    public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt) {
        if (this.initialization) {
            if ((paramRadioGroup.equals(this.mode_selector1)) && (this.mode1run)) {
                this.mode2run = false;
                this.mode_selector2.clearCheck();
            }
            switch (paramInt) {
                default:
                    this.mode2run = true;
                    if ((!paramRadioGroup.equals(this.mode_selector2)) || (!this.mode2run))
                        break;
                    this.mode1run = false;
                    this.mode_selector1.clearCheck();
                    switch (paramInt) {
                        default:
                        case 2131230730:
                        case 2131230731:
                    }
                case 2131230727:
                case 2131230728:
            }
        }
        while (true) {
            this.mode1run = true;
            this.standing_btn.setEnabled(true);
            this.inpocket_btn.setEnabled(true);
            this.lying_btn.setEnabled(true);
            this.jumping_btn.setEnabled(true);
            this.walking_btn.setEnabled(true);
            this.running_btn.setEnabled(true);
            this.upstair_btn.setEnabled(true);
            this.downstair_btn.setEnabled(true);
            this.upelevator_btn.setEnabled(true);
            this.dowbelevator_btn.setEnabled(true);
            this.sitting_btn.setEnabled(true);
            this.tv_btn.setEnabled(true);
            this.drivecar_btn.setEnabled(true);
            this.ridebus_btn.setEnabled(true);
            this.ridebike_btn.setEnabled(true);
            return;
//      this.isLeft = true;
//      this.isRight = false;
//      this.isJacket = false;
//      this.position = "Trouser Front";
//      break;
//      this.isLeft = false;
//      this.isRight = true;
//      this.isJacket = false;
//      this.position = "Trouser Back";
//      break;
//      this.isLeft = true;
//      this.isRight = false;
//      this.isJacket = false;
//      this.position = "Jacket Side";
//      continue;
//      this.isLeft = false;
//      this.isRight = true;
//      this.isJacket = false;
//      this.position = "Jacket Inner";
        }
    }

    public void onClick(View paramView) {
        this.sub_name = this.name_edt.getText().toString();
        switch (paramView.getId()) {
            default:
                return;
            case 2131230732:
                this.state = "Idle (SittingORStanding)";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230733:
                this.state = "Walking on Treadmill";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230734:
                this.state = "Running on Treadmill";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230735:
                this.state = "Jumping";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230736:
                this.state = "Walking";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230737:
                this.state = "Running";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230738:
                this.state = "Going UpStairs";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230739:
                this.state = "Going DownStairs";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230740:
                this.state = "Elevator-up";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230741:
                this.state = "Elevator-down";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230742:
                this.state = "Vaccuming";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230743:
                this.state = "WatchingTV";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230744:
                this.state = "DrivingCar";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230745:
                this.state = "RidingBus";
                this.intent = new Intent(this, DetermingActivity.class);
                this.intent.putExtra("isLeft", this.isLeft);
                this.intent.putExtra("isRight", this.isRight);
                this.intent.putExtra("isJacket", this.isJacket);
                this.intent.putExtra("position", this.position);
                this.intent.putExtra("state", this.state);
                this.intent.putExtra("sub_name", this.sub_name);
                startActivity(this.intent);
                return;
            case 2131230746:
        }
        this.state = "RidingBike";
        this.intent = new Intent(this, DetermingActivity.class);
        this.intent.putExtra("isLeft", this.isLeft);
        this.intent.putExtra("isRight", this.isRight);
        this.intent.putExtra("isJacket", this.isJacket);
        this.intent.putExtra("position", this.position);
        this.intent.putExtra("state", this.state);
        this.intent.putExtra("sub_name", this.sub_name);
        startActivity(this.intent);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(2130903041);
        this.name_edt = ((EditText) findViewById(2131230725));
        this.mode_selector1 = ((RadioGroup) findViewById(2131230726));
        this.mode_selector2 = ((RadioGroup) findViewById(2131230729));
        this.standing_btn = ((Button) findViewById(2131230732));
        this.inpocket_btn = ((Button) findViewById(2131230733));
        this.lying_btn = ((Button) findViewById(2131230734));
        this.jumping_btn = ((Button) findViewById(2131230735));
        this.walking_btn = ((Button) findViewById(2131230736));
        this.running_btn = ((Button) findViewById(2131230737));
        this.upstair_btn = ((Button) findViewById(2131230738));
        this.downstair_btn = ((Button) findViewById(2131230739));
        this.upelevator_btn = ((Button) findViewById(2131230740));
        this.dowbelevator_btn = ((Button) findViewById(2131230741));
        this.sitting_btn = ((Button) findViewById(2131230742));
        this.tv_btn = ((Button) findViewById(2131230743));
        this.drivecar_btn = ((Button) findViewById(2131230744));
        this.ridebus_btn = ((Button) findViewById(2131230745));
        this.ridebike_btn = ((Button) findViewById(2131230746));
        this.mode_selector1.setOnCheckedChangeListener(this);
        this.mode_selector2.setOnCheckedChangeListener(this);
        this.standing_btn.setOnClickListener(this);
        this.inpocket_btn.setOnClickListener(this);
        this.lying_btn.setOnClickListener(this);
        this.jumping_btn.setOnClickListener(this);
        this.walking_btn.setOnClickListener(this);
        this.running_btn.setOnClickListener(this);
        this.upstair_btn.setOnClickListener(this);
        this.downstair_btn.setOnClickListener(this);
        this.upelevator_btn.setOnClickListener(this);
        this.dowbelevator_btn.setOnClickListener(this);
        this.sitting_btn.setOnClickListener(this);
        this.tv_btn.setOnClickListener(this);
        this.drivecar_btn.setOnClickListener(this);
        this.ridebus_btn.setOnClickListener(this);
        this.ridebike_btn.setOnClickListener(this);
        this.standing_btn.setEnabled(false);
        this.inpocket_btn.setEnabled(false);
        this.lying_btn.setEnabled(false);
        this.jumping_btn.setEnabled(false);
        this.walking_btn.setEnabled(false);
        this.running_btn.setEnabled(false);
        this.upstair_btn.setEnabled(false);
        this.downstair_btn.setEnabled(false);
        this.upelevator_btn.setEnabled(false);
        this.dowbelevator_btn.setEnabled(false);
        this.sitting_btn.setEnabled(false);
        this.tv_btn.setEnabled(false);
        this.drivecar_btn.setEnabled(false);
        this.ridebus_btn.setEnabled(false);
        this.ridebike_btn.setEnabled(false);
        this.mode_selector1.clearCheck();
        this.mode_selector2.clearCheck();
        this.initialization = true;
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(2131165185, paramMenu);
        return true;
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.example.monitoringactivity.MainActivity
 * JD-Core Version:    0.6.0
 */