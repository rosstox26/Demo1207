package com.ateachr.tox26.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SalesActivity extends Activity implements View.OnClickListener{

    private Button buttonBuy;
    private EditText editTextProduct;
    private EditText editTextPrice;
    private EditText editTextQuantity;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        buttonBuy = (Button) findViewById(R.id.buttonBuy);
        editTextProduct = (EditText) findViewById(R.id.editTextProduct);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);

        buttonBuy.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(SalesActivity.this, "User signed in: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SalesActivity.this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SalesActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {

        String product = editTextProduct.getText().toString();
        String price = editTextPrice.getText().toString();
        String quantity = editTextQuantity.getText().toString();

        Purchase purchase = new Purchase(product, price, quantity);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataPurchases = database.getReference("purchases");
        //dataPurchases.child("1").setValue(purchase);
        DatabaseReference dataNewPurchase = dataPurchases.push();
        dataNewPurchase.setValue(purchase);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intentMonitor = new Intent(SalesActivity.this, MonitorActivity.class);
        //Intent intentPurchase = new Intent(MonitorActivity.this, SalesActivity.class);
        if (mAuth.getCurrentUser() != null ) {
            if (item.getItemId() == R.id.menuLogout) {
                mAuth.signOut();

            } else if (item.getItemId() == R.id.menuMonitor) {
                startActivity(intentMonitor);

            } else if (item.getItemId() == R.id.menuPurchase) {

                Toast.makeText(this, "You are in Purchase Page Already", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
