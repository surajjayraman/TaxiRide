package ielite.app.myriderequest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private SessionConfiguration configuration;
    private RideRequestButton requestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configuration = new SessionConfiguration.Builder()
                // mandatory
                .setClientId("YOUR_CLIENT_ID")
                // required for enhanced button features
                .setServerToken("YOUR_SERVER_TOKEN")
                // required for implicit grant authentication
                .setRedirectUri("YOUR_REDIRECT_URI")
                // required scope for Ride Request Widget features
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                // optional: set Sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();

        UberSdk.initialize(configuration);

        // get the context by invoking ``getApplicationContext()``, ``getContext()``, ``getBaseContext()`` or ``this`` when in the activity class
        requestButton = new RideRequestButton(this);
        // get your layout
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
       // layout.addView(requestButton);

        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
                .setDropoffLocation(37.775304d, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation(37.775304d, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location.
                .setDropoffLocation(37.795079d, -122.4397805, "Embarcadero", "One Embarcadero Center, San Francisco")
                .build();
        // set parameters for the RideRequestButton instance
        requestButton.setRideParameters(rideParams);

        ServerTokenSession session = new ServerTokenSession(configuration);
        requestButton.setSession(session);
        requestButton.loadRideInformation();

        RideRequestButton rideWidgetButton = new RideRequestButton(this);
        layout.addView(rideWidgetButton);

        int requestCode = 1234;
        rideWidgetButton.setRequestBehavior(new RideRequestActivityBehavior(this, requestCode));

        // Optional, default behavior is to use current location for pickup
        RideParameters rideWidgetParams = new RideParameters.Builder()
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                .setPickupLocation(37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .setDropoffLocation(37.795079, -122.4397805, "Embarcadero", "One Embarcadero Center, San Francisco")
                .build();
        rideWidgetButton.setRideParameters(rideWidgetParams);



    }
}
