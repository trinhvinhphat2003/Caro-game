package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.api.PaymentRepository;
import com.example.myapplication.api.UserRepository;
import com.example.myapplication.model.RechargeOption;
import com.example.myapplication.model.request.LoginRequest;
import com.example.myapplication.model.request.PaymentRequest;
import com.example.myapplication.model.response.LoginResponse;
import com.example.myapplication.model.response.PaymentResponse;
import com.example.myapplication.model.response.UserResponse;
import com.example.myapplication.services.PaymentService;
import com.example.myapplication.services.UserService;
import com.example.myapplication.tokenManager.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.ViewHolder> {
    private List<RechargeOption> rechargeOptions;
    private Context context;
    private WebView webView;

    public RechargeAdapter(Context context, List<RechargeOption> rechargeOptions, WebView webView) {
        this.rechargeOptions = rechargeOptions;
        this.context = context;
        this.webView = webView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recharge_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RechargeOption option = rechargeOptions.get(position);
        holder.priceTextView.setText(option.getPrice() + " VND");
        holder.coinsTextView.setText(option.getCoins() + " coins");
        holder.imageView.setImageResource(option.getImageResId());
        holder.itemView.setOnClickListener(v -> {
            showVNPayPage(option);
            //handleAddCoin(option);
        });
    }

    private void showVNPayPage(RechargeOption option) {
        // Hiển thị WebView
        webView.setVisibility(View.VISIBLE);

        // Thiết lập các cài đặt cho WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        // Thiết lập WebViewClient để xử lý sự kiện trong WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Kiểm tra khi WebView đã load xong
                if (url != null && url.contains("return_url_trigger")) {
                    // Xử lý khi nhận được return URL từ SDK
                    webView.setVisibility(WebView.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Xử lý logic khi URL được load trong WebView
                System.out.println(url);
                if (url != null && url.contains("vnp_TransactionStatus=00")) {
                    // Xử lý khi nhận được return URL từ SDK
                    webView.setVisibility(WebView.GONE);
                    handleAddCoin(option);
                    //Toast.makeText(context, "Transaction successful", Toast.LENGTH_SHORT).show();
                    System.out.println("//////////////////Transaction successful///////////////////////");
                    return true; // Đã xử lý sự kiện, không cần load URL nữa
                } else if (url != null && url.contains("vnp_TransactionStatus=02")) {
                    System.out.println("//////////////////Transaction Cancel///////////////////////");
                    webView.setVisibility(WebView.GONE);
                    Toast.makeText(context, "Cancel transaction", Toast.LENGTH_SHORT).show();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        //webView.setWebChromeClient(new WebChromeClient());

        // Load URL của SDK vào WebView
        String vnPayURL = buildPaymentUrl(option.getPrice());
        System.out.println(vnPayURL);
        webView.loadUrl(vnPayURL);
    }

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildPaymentUrl(int amount) {
        try {
            String tmnCode = "0HMW9F90";
            String secretKey = "4IKW54V2L7D1RN4L8PDKTP2Y2UP3O9BI";
            String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
            String returnUrl = "intent://resultactivity";
            String orderId = new SimpleDateFormat("ddHHmmss").format(new Date());
            String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            Map<String, String> vnp_Params = new TreeMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", tmnCode);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", orderId);
            vnp_Params.put("vnp_OrderInfo", "ThanhtoanchomaGD:" + orderId);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
            vnp_Params.put("vnp_ReturnUrl", returnUrl);
            vnp_Params.put("vnp_IpAddr", "192.168.88.143");
            vnp_Params.put("vnp_CreateDate", createDate);

            String queryString = vnp_Params.entrySet().stream()
                    .map(e -> e.getKey() + "=" + encodeValue(e.getValue()))
                    .reduce((e1, e2) -> e1 + "&" + e2)
                    .orElse("");

            //String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
            //String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hash_Data.toString());
            System.out.println(queryString);
            String secureHash = hmacSHA512(secretKey, queryString);
            vnp_Params.put("vnp_SecureHash", secureHash);
            queryString += "&vnp_SecureHash=";
            queryString += secureHash;

//            queryString = vnp_Params.entrySet().stream()
//                    .map(e -> e.getKey() + "=" + encodeValue(e.getValue()))
//                    .reduce((e1, e2) -> e1 + "&" + e2)
//                    .orElse("");

            return vnpUrl + "?" + queryString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    private void handleAddCoin(RechargeOption option) {
        PaymentService paymentService = PaymentRepository.getPaymentService();
        PaymentRequest paymentResquest = new PaymentRequest(option.getCoins());

        try {
            Call<PaymentResponse> call = paymentService.createPayment("Bearer " + TokenManager.getToken(), paymentResquest);
            call.enqueue(new Callback<PaymentResponse>() {
                @Override
                public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                    if (response.body() != null){
                        PaymentResponse paymentResponse = response.body();
                        if (paymentResponse.getOnSuccess()) {
                            JSONObject user = TokenManager.getUserObject();
                            try {
                                user.put("wallet", String.valueOf(Integer.valueOf((String)user.get("wallet")).intValue() + option.getCoins()));
                            } catch (JSONException e) {
                                Toast.makeText(context, "Failed to add coins", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(context, "Add coin successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to add coins", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<PaymentResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return rechargeOptions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView priceTextView;
        TextView coinsTextView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            coinsTextView = itemView.findViewById(R.id.coinsTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}