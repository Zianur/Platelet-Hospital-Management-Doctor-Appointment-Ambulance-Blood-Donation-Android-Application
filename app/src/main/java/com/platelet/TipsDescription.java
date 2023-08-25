package com.platelet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class TipsDescription extends AppCompatActivity {

    String tipsName;
    TextView tipsNameTextView, tipsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_description);

        tipsNameTextView = findViewById(R.id.tipsNameId);
        tipsDescription = findViewById(R.id.textDiscriptionId);


        tipsName = getIntent().getStringExtra("tips");

        tipsNameTextView.setText(tipsName);



        tipsName = tipsName.replaceAll(" ", "");
//        Toast.makeText(this, tipsName, Toast.LENGTH_SHORT).show();



        if(tipsName.equals("Avoidsugarandsugaryproducts"))
        {
            tipsDescription.setText(getResources().getString(R.string.Avoidsugarandsugaryproducts));
        }
        if(tipsName.equals("Avoidextrasalt"))
        {
            tipsDescription.setText(getResources().getString(R.string.Avoidextrasalt));
        }
        if(tipsName.equals("Avoidjunkfoods"))
        {
            tipsDescription.setText(getResources().getString(R.string.Avoidjunkfoods));
        }
        if(tipsName.equals("Keepyourselfneatandclean"))
        {
            tipsDescription.setText(getResources().getString(R.string.Keepyourselfneatandclean));
        }
        if(tipsName.equals("Maintainahealthyweight"))
        {
            tipsDescription.setText(getResources().getString(R.string.Maintainahealthyweight));
        }
        if(tipsName.equals("Consuminghealthyfoodandbeverages"))
        {
            tipsDescription.setText(getResources().getString(R.string.Consuminghealthyfoodandbeverages));
        }
        if(tipsName.equals("Movemoreandsitlessthroughouttheday"))
        {
            tipsDescription.setText(getResources().getString(R.string.Movemoreandsitlessthroughouttheday));
        }
        if(tipsName.equals("Getadequatesleep"))
        {
            tipsDescription.setText(getResources().getString(R.string.Getadequatesleep));
        }
        if(tipsName.equals("Considereatingmoreprotein"))
        {
            tipsDescription.setText(getResources().getString(R.string.Considereatingmoreprotein));
        }
        if(tipsName.equals("Eatseasonalfruitsmore"))
        {
            tipsDescription.setText(getResources().getString(R.string.Eatseasonalfruitsmore));
        }
        if(tipsName.equals("Avoidtechneck"))
        {
            tipsDescription.setText(getResources().getString(R.string.Avoidtechneck));
        }
        if(tipsName.equals("OptimiseyourTVtime"))
        {
            tipsDescription.setText(getResources().getString(R.string.OptimiseyourTVtime));
        }
        if(tipsName.equals("Checkyourbloodpressureregularly"))
        {
            tipsDescription.setText(getResources().getString(R.string.Checkyourbloodpressureregularly));
        }
        if(tipsName.equals("Coveryourmouthwhencoughingorsneezing"))
        {
            tipsDescription.setText(getResources().getString(R.string.Coveryourmouthwhencoughingorsneezing));
        }
        if(tipsName.equals("Preventmosquitobites"))
        {
            tipsDescription.setText(getResources().getString(R.string.Preventmosquitobites));
        }
        if(tipsName.equals("Takeantibioticsonlyasprescribed"))
        {
            tipsDescription.setText(getResources().getString(R.string.Takeantibioticsonlyasprescribed));
        }
        if(tipsName.equals("Prepareyourfoodcorrectly"))
        {
            tipsDescription.setText(getResources().getString(R.string.Prepareyourfoodcorrectly));
        }
        if(tipsName.equals("Haveregularcheckups"))
        {
            tipsDescription.setText(getResources().getString(R.string.Haveregularcheckups));
        }

    }
}