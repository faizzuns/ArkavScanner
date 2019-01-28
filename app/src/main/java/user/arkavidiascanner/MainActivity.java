package user.arkavidiascanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtEvent;
    TextView txtName;
    ProgressBar progressBar;
    Button button;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = findViewById(R.id.txt_name);
        txtEvent = findViewById(R.id.txt_event);
        progressBar = findViewById(R.id.progress);
        button = findViewById(R.id.btn);

        txtName.setVisibility(View.GONE);
        txtEvent.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        button.setText("Scan");
        button.setVisibility(View.VISIBLE);
    }

    public void process(View view) {
        boolean allowed = checkPermission(Manifest.permission.CAMERA, 100);
        if (allowed) {
            if (txtName.getVisibility() == View.GONE) {
                startActivityForResult(new Intent(getApplicationContext(), ScannerActivity.class), 1);
            } else {
                button.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                User user = new User();
                user.setToken(token);
                Call<BaseResponse> call = RetrofitServices.sendRequest().confirmUser("application/json",
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiYWRtaW4iOnRydWUsImlhdCI6MTU0ODY0NjI3MX0.FfMJGA-aU4lmyUzYOWma6kDtrQaA63KY6TlUiPf0V0w",
                        user);
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("success")) {
                                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                txtEvent.setVisibility(View.GONE);
                                txtName.setVisibility(View.GONE);
                                button.setText("Scan");
                            } else {
                                Toast.makeText(MainActivity.this, "User gagal di konfirmasi", Toast.LENGTH_SHORT).show();
                            }
                            button.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Something was error", Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            final String token = data.getStringExtra("token");

            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Call<BaseResponse> call = RetrofitServices.sendRequest().callUser(token, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiYWRtaW4iOnRydWUsImlhdCI6MTU0ODY0NjI3MX0.FfMJGA-aU4lmyUzYOWma6kDtrQaA63KY6TlUiPf0V0w");
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null && response.body().getStatus().equals("success")) {
                        User user = response.body().getUser();
                        user.setToken(token);
                        MainActivity.this.token = token;
                        txtName.setText(user.getName());
                        txtEvent.setText(user.getEvent());
                        button.setText("Confirm");
                        txtName.setVisibility(View.VISIBLE);
                        txtEvent.setVisibility(View.VISIBLE);
                    }
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Something was error", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startActivityForResult(new Intent(getApplicationContext(), ScannerActivity.class), 1);
        } else {
            Toast.makeText(this, "You must allow Camera permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public boolean checkPermission(String permission, int permissionCode) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission}, permissionCode);
                return false;
            }
        }
        return true;
    }
}
