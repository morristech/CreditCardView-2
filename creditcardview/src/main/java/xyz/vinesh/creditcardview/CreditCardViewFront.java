package xyz.vinesh.creditcardview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vineshraju on 24/6/16.
 */
public class CreditCardViewFront extends Fragment implements CardUpdateListener {

    private TextView number, name, expiry;
    private ImageView logo;
    private Typeface typeface;
    private Card card;
    private CardView cardView;
    private CardTypes cardTypes;
    private static CreditCardView creditCardView;

    public CreditCardViewFront() {
    }

    public static CreditCardViewFront newInstance(Card card) {
        CreditCardViewFront fragment = new CreditCardViewFront();
        Bundle args = new Bundle();
        args.putString(Card.NAME_KEY, card.getCardHolderName());
        args.putString(Card.NUMBER_KEY, card.getCardNumber());
        args.putString(Card.EXPIRY_KEY, card.getExpiry());
        args.putString(Card.CVV_KEY, card.getCvv());
        args.putInt(Card.COLOR_KEY, card.getCardForegroundColor());
        fragment.setArguments(args);
        return fragment;
    }

    public void setCreditCardView(CreditCardView creditCardView) {
        this.creditCardView = creditCardView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        card = new Card(args.getString(Card.NAME_KEY), args.getString(Card.NUMBER_KEY), args.getString(Card.CVV_KEY), args.getString(Card.EXPIRY_KEY), args.getInt(Card.COLOR_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.credit_card_view_front, container, false);
        number = (TextView) view.findViewById(R.id.tvCardNumber);
        name = (TextView) view.findViewById(R.id.tvName);
        expiry = (TextView) view.findViewById(R.id.tvExpiry);
        logo = (ImageView) view.findViewById(R.id.ivLogo);
        cardView = (CardView) view.findViewById(R.id.card_view);
        if (creditCardView != null)
            creditCardView.setFrontListener(this);

        update();

        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ocraextended.ttf");
        number.setTypeface(typeface);
        name.setTypeface(typeface);
        expiry.setTypeface(typeface);

        cardTypes = new CardTypes(getContext());

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshLogo(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void update() {
        cardView.setCardBackgroundColor(card.getCardForegroundColor());
        number.setText(introduceGaps(card.getCardNumber()));
        name.setText(card.getCardHolderName());
        expiry.setText(card.getExpiry());
    }


    private void refreshLogo(CharSequence s) {
        boolean matched = false;
        ArrayList<CardTypes.PatternResourcePairs> cardTypes = CreditCardViewFront.this.cardTypes.getCardTypes();
        for (CardTypes.PatternResourcePairs cardType : cardTypes) {
            if (cardType.matches(s.toString())) {
                matched = true;
                logo.setImageDrawable(cardType.getLogoResource());
                break;
            }
            if (!matched) logo.setImageDrawable(null);
        }
    }


    private String introduceGaps(String nonGappedNumber) {
        int count = 0;
        String gappedNumber = "";
        for (char c : nonGappedNumber.toCharArray()) {
            if (count < 4) {
                gappedNumber += c;
                count++;
            } else {
                gappedNumber = gappedNumber + " " + c;
                count = 1;
            }
        }
        return gappedNumber;
    }

    @Override
    public void updateCard(Card card) {
        this.card = card;
        update();
    }

    @Override
    public void updateCardTypes(CardTypes cardTypes) {
        this.cardTypes = cardTypes;
    }
}
