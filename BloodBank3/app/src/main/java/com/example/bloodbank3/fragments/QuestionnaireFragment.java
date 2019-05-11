package com.example.bloodbank3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bloodbank3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionnaireFragment extends Fragment {

    private View view;
    private TextView que1,que2,que3,que4,que5,que6,que7,que8;
    private Button submitButton;
    private RadioButton que1yes,que1no;
    private RadioButton que2yes,que2no;
    private RadioButton que3yes,que3no;
    private RadioButton que4yes,que4no;
    private RadioButton que5yes,que5no;
    private RadioButton que6yes,que6no;
    private RadioButton que7yes,que7no;
    private RadioButton que8yes,que8no;
    private DatabaseReference db_ref;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.questionnaire, container, false);

        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle("Questionnaire");
        db_ref = FirebaseDatabase.getInstance().getReference();


        que1 = view.findViewById(R.id.ques1);
        que2 = view.findViewById(R.id.ques2);
        que3 = view.findViewById(R.id.ques3);
        que4 = view.findViewById(R.id.ques4);
        que5 = view.findViewById(R.id.ques5);
        que6 = view.findViewById(R.id.ques6);
        que7 = view.findViewById(R.id.ques7);
        que8 = view.findViewById(R.id.ques8);
        submitButton = view.findViewById(R.id.submitButton);

        que1yes = view.findViewById(R.id.que1yes);
        que1no = view.findViewById(R.id.que1no);

        que2yes = view.findViewById(R.id.que2yes);
        que2no = view.findViewById(R.id.que2no);

        que3yes = view.findViewById(R.id.que3yes);
        que3no = view.findViewById(R.id.que3no);

        que4yes = view.findViewById(R.id.que4yes);
        que4no = view.findViewById(R.id.que4no);

        que5yes = view.findViewById(R.id.que5yes);
        que5no = view.findViewById(R.id.que5no);

        que6yes = view.findViewById(R.id.que6yes);
        que6no = view.findViewById(R.id.que6no);

        que7yes = view.findViewById(R.id.que7yes);
        que7no = view.findViewById(R.id.que7no);

        que8yes = view.findViewById(R.id.que8yes);
        que8no = view.findViewById(R.id.que8no);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(que1yes.isChecked())
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans1").setValue(que1yes.getText().toString());
                else
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans1").setValue(que1no.getText().toString());

                if(que2yes.isChecked())
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans2").setValue(que2yes.getText().toString());
                else
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans2").setValue(que2no.getText().toString());


                if(que3yes.isChecked())
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans3").setValue(que3yes.getText().toString());
                else
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans3").setValue(que3no.getText().toString());

                if(que4yes.isChecked())
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans4").setValue(que4yes.getText().toString());
                else
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans4").setValue(que4no.getText().toString());


                if(que5yes.isChecked())
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans5").setValue(que5yes.getText().toString());
                else
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans5").setValue(que5no.getText().toString());

                if(que6yes.isChecked())
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans6").setValue(que6yes.getText().toString());
                else
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans6").setValue(que6no.getText().toString());


                if(que7yes.isChecked())
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans7").setValue(que7yes.getText().toString());
                else
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans7").setValue(que7no.getText().toString());

                if(que8yes.isChecked())
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans8").setValue(que8yes.getText().toString());
                else
                    db_ref.child("questionnaire").child(mAuth.getCurrentUser().getUid()).child("ans8").setValue(que8no.getText().toString());



            }
        });


        return view;
    }
}
