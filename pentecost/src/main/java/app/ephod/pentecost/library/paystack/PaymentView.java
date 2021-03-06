package app.ephod.pentecost.library.paystack;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.ephod.pentecost.pentecost.R;

public class PaymentView extends LinearLayout {

    private Context mContext;
    private AttributeSet attributeSet;
    private int styleAttr;
    private View view;

    Drawable headerSrc;
    String theme = "0";
    Drawable background;
    String backgroundColor;
    String billHeader;
    String billContent;

    EditText creditNumber;
    EditText creditMonth;
    EditText creditCCV;



    Button payButton;
    ProgressBar progressBar;


    int formerLength = 0;

    public Button getPayButton() {
        return payButton;
    }

    Integer[] imageArray = {R.drawable.visa, R.drawable.mastercard, R.drawable.discover, R.drawable.american_express};

    public void setCardNumber(String mCreditNumber) {
        this.creditNumber.setText(mCreditNumber);
    }

    public void setCardExpDate(String mCreditMonth) {
        this.creditMonth.setText(mCreditMonth);
    }

    public void setCardCCV(String mCreditCCV) {
        this.creditCCV.setText(mCreditCCV);
    }

    public String getCardNumber() {
        return creditNumber.getText().toString();
    }

    public String getCardExpDate() {
        return creditMonth.getText().toString();
    }

    public String getCardCCV() {
        return creditCCV.getText().toString();
    }

    public PaymentView(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public PaymentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.attributeSet = attrs;
        initView(context);
    }

    public PaymentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.attributeSet = attrs;
        this.styleAttr = defStyleAttr;
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PaymentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        this.attributeSet = attrs;
        this.styleAttr = defStyleAttr;
        initView(context);
    }

    public void setHeaderSrc(Drawable headerSrc) {
        this.headerSrc = headerSrc;
    }

    public void setPentecostTheme(String theme) {
        this.theme = theme;
    }

    public void setBillHeader(String billHeader) {
        this.billHeader = billHeader;
    }

    public void setBillContent(String billContent) {
        this.billContent = billContent;
    }

    public void setPentecostBackground(Drawable background) {
        this.background = background;
    }

    public void setPentecostBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void showLoader(){
        progressBar.setVisibility(VISIBLE);
        payButton.setVisibility(INVISIBLE);
        payButton.setEnabled(false);

        creditNumber.setEnabled(false);
        creditMonth.setEnabled(false);
        creditCCV.setEnabled(false);
        //payButton.blockTouch();
        //payButton.morphToProgress(R.color.white, R.dimen.size_2, width, R.dimen.size_14, 10, R.color.colorAccent);
    }

    public void hideLoader(){
        //payButton.unblockTouch();
        progressBar.setVisibility(GONE);
        payButton.setVisibility(VISIBLE);
        payButton.setEnabled(true);

        creditNumber.setEnabled(true);
        creditMonth.setEnabled(true);
        creditCCV.setEnabled(true);
    }

    private void tintProgressBar(ProgressBar mProgressBar){
        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable wrapDrawable = DrawableCompat.wrap(mProgressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getContext(), R.color.colorPayAccent));
            mProgressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPayAccent), PorterDuff.Mode.SRC_IN);
        }
    }

    private void initView(Context context){

        //inflate layout
        View view = inflate(context, R.layout.paymentview, this);

        //init views
        ImageView headerView = view.findViewById(R.id.header_view);
        RelativeLayout parentView = view.findViewById(R.id.parent_view);
        //EditText creditEdit = view.findViewById(R.id.credit_card_number);
        //EditText ccvEdit = view.findViewById(R.id.credit_card_ccv);
        //EditText dateEdit = view.findViewById(R.id.credit_card_expiry);
        ImageView secureLogo = view.findViewById(R.id.secure_logo);

        LinearLayout secondParentView = view.findViewById(R.id.second_parent);

        TextView billHeaderText = view.findViewById(R.id.bill_header);
        TextView billHeaderContent = view.findViewById(R.id.bill_content);

        creditNumber = findViewById(R.id.credit_card_number);
        creditMonth = findViewById(R.id.credit_card_expiry);
        creditCCV = findViewById(R.id.credit_card_ccv);

        payButton = findViewById(R.id.pay_button);
        progressBar = findViewById(R.id.progress_bar);

        tintProgressBar(progressBar);
        setTextWatchers();

        //we are setting the background resource here again because MorphButton overrides what is set in the xml
        payButton.setBackgroundResource(R.drawable.payment_button);

        TypedArray arr = mContext.obtainStyledAttributes(attributeSet, R.styleable.PaymentView, styleAttr, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            headerView.setClipToOutline(true);
            parentView.setClipToOutline(true);
        }

        theme = arr.getString(R.styleable.PaymentView_pentecostTheme);
        background = arr.getDrawable(R.styleable.PaymentView_pentecostBackgroundDrawable);
        backgroundColor = arr.getString(R.styleable.PaymentView_pentecostBackgroundColor);
        headerSrc = arr.getDrawable(R.styleable.PaymentView_pentecostHeaderSrc);

        if (theme == null){
            theme = "0";
        }

        arr.recycle();

        if (theme.equals("0")){
            parentView.setBackgroundResource(R.drawable.round_dark_bg);
            creditNumber.setBackgroundResource(R.drawable.edit_text_bg);
            creditCCV.setBackgroundResource(R.drawable.edit_text_bg);
            creditMonth.setBackgroundResource(R.drawable.edit_text_bg);
            secureLogo.setImageResource(R.drawable.white_paystack_logo);
            billHeaderText.setTextColor(getResources().getColor(R.color.white));
            billHeaderContent.setTextColor(getResources().getColor(R.color.white));
        } else if (theme.equals("1")) {
            parentView.setBackgroundResource(R.drawable.round_white_bg);
            creditNumber.setBackgroundResource(R.drawable.edit_text_white_bg);
            creditCCV.setBackgroundResource(R.drawable.edit_text_white_bg);
            creditMonth.setBackgroundResource(R.drawable.edit_text_white_bg);
            secureLogo.setImageResource(R.drawable.blue_paystack_logo);
            billHeaderText.setTextColor(getResources().getColor(R.color.black));
            billHeaderContent.setTextColor(getResources().getColor(R.color.black));
        }

        Log.e("TAG", theme);

        if (background != null){
            secondParentView.setBackgroundResource(0);
            parentView.setBackground(background);
        }

        if (backgroundColor != null){

            int bgColor = Color.parseColor(backgroundColor);
            secondParentView.setBackgroundResource(0);
            parentView.setBackgroundColor(bgColor);
        }
    }

    public static ArrayList<String> listOfPattern()
    {
        ArrayList<String> listOfPattern=new ArrayList<String>();

        String ptVisa = "^4[0-9]$";

        listOfPattern.add(ptVisa);

        String ptMasterCard = "^5[1-5]$";

        listOfPattern.add(ptMasterCard);

        String ptDiscover = "^6(?:011|5[0-9]{2})$";

        listOfPattern.add(ptDiscover);

        String ptAmeExp = "^3[47]$";

        listOfPattern.add(ptAmeExp);

        return listOfPattern;
    }



    private void setTextWatchers() {

        creditNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int j, int j1, int j2) {

                String cardNumber = charSequence.toString();

                if (cardNumber.length() > 2){

                    for (int i = 0; i <  listOfPattern().size(); i++){

                        if (cardNumber.substring(0, 2).matches(listOfPattern().get(i))){

                            creditNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,  imageArray[i], 0);
                            //break;

                        } else {
                            //creditNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.visa, 0);
                            //break;

                        }

                    }

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String cardNumber = creditNumber.getText().toString();

                if (!cardNumber.equalsIgnoreCase("")){

                    for (int i = 0; i < listOfPattern().size(); i++){

                        if (cardNumber.matches(listOfPattern().get(i))){
                            creditNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,  imageArray[i], 0);
                        }

                    }

                } else {

                    creditNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,  0, 0);

                }

            }
        });

        creditMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                formerLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > formerLength){ //User is adding text
                    if(editable.length() == 2){
                        editable.append("/");
                    }
                } else {
                    if(editable.length() == 2){
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }

            }
        });


    }
}
