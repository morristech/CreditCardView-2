package xyz.vinesh.creditcardview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vineshraju on 24/6/16.
 */
public class CreditCardViewBack extends Fragment implements CardUpdateListener {

    private TextView cvv;
    private ImageView logo;
    private CardView cardView;
    private CardTypes cardTypes;
    private Card card;

    private Typeface typeface;
    private CreditCardView creditCardView;

    public CreditCardViewBack() {
    }

    public static CreditCardViewBack newInstance(Card card) {
        CreditCardViewBack fragment = new CreditCardViewBack();
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
        View view = inflater.inflate(R.layout.credit_card_view_back, container, false);
        cvv = (TextView) view.findViewById(R.id.tvCvv);
        logo = (ImageView) view.findViewById(R.id.ivLogo);
        cardView = (CardView) view.findViewById(R.id.card_view);
        if (creditCardView != null)
            creditCardView.setBackListener(this);

        cardTypes = new CardTypes(getContext());

        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ocraextended.ttf");
        cvv.setTypeface(typeface);

        update();

        return view;
    }

    private void refreshLogo(CharSequence s) {
        boolean matched = false;
        ArrayList<CardTypes.PatternResourcePairs> cardTypes = CreditCardViewBack.this.cardTypes.getCardTypes();
        for (CardTypes.PatternResourcePairs cardType : cardTypes) {
            if (cardType.matches(s.toString())) {
                matched = true;
                logo.setImageDrawable(cardType.getLogoResource());
                break;
            }
            if (!matched) logo.setImageDrawable(null);
        }
    }


    private void update() {
        cardView.setCardBackgroundColor(card.getCardForegroundColor());
        cvv.setText(card.getCvv());
        refreshLogo(card.getCardNumber());
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
