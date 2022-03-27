package com.example.d3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;

import com.example.d3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "com.example.d3.URL";

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        if(getSupportActionBar()!=null)
        getSupportActionBar().hide();
        binding.urlValue.setText("http://20.204.52.253:3000/preview/623dca08fcb8d52c0b482c3a");
        setGoClickListener();
    }

    private void setGoClickListener() {
        binding.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=binding.urlValue.getText().toString().trim();
                if(validate(url)) {
                    hideKeyboard(view);
                    showWebActivity(url);
                }
                else
                    return;
            }
        });
    }

    private void showWebActivity(String url) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        startActivity(intent);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private boolean validate(String url) {
        if (url.isEmpty()) {
            binding.urlValue.setError(getString(R.string.err_enter_url));
            return false;
        }
        if (!URLUtil.isValidUrl(url)) {
            binding.urlValue.setError(getString(R.string.err_invalid_url));
            return false;
        }
        return true;
    }
}