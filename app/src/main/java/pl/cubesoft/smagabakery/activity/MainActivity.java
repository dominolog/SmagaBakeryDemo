package pl.cubesoft.smagabakery.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.smagabakery.ImageLoader;
import pl.cubesoft.smagabakery.PersonController;
import pl.cubesoft.smagabakery.R;
import pl.cubesoft.smagabakery.SmagaBakeryAPIService;
import pl.cubesoft.smagabakery.SmagaBakeryApplication;
import pl.cubesoft.smagabakery.adapter.PersonListAdapter;
import pl.cubesoft.smagabakery.dialog.AlertDialogEvent;
import pl.cubesoft.smagabakery.dialog.AlertDialogFragment;
import pl.cubesoft.smagabakery.model.GetContactsResult;
import pl.cubesoft.smagabakery.model.Person;
import pl.cubesoft.smagabakery.view.EmptyRecyclerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements PersonListAdapter.OnPersonListAdapterInteractionListener {

    private static final Object IMAGE_LOAD_TAG = "PersonList";
    private static final int REQUEST_DELETE = 100;
    private static final int REQUEST_SELECT_COLOR = 101;
    private static final String PARAM_POSITION = "position";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler)
    EmptyRecyclerView recycler;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;


    @BindView(R.id.empty)
    View emptyView;

    @Inject
    ImageLoader imageLoader;

    @Inject
    PersonController personController;

    @Inject
    EventBus eventBus;

    private LinearLayoutManager layoutManager;
    private PersonListAdapter adapter;


    static class CellColor {
        int color;
        String colorName;

        public CellColor(int color, String colorName) {
            this.color = color;
            this.colorName = colorName;
        }
    }

    private static final List<CellColor> CELL_COLORS =Arrays.asList(new CellColor(Color.rgb(128, 0, 0), "Red"), new CellColor(Color.GREEN, "Green"), new CellColor(Color.BLUE, "Blue"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getMyApplication().getAppComponent().inject(this);
        setSupportActionBar(toolbar);


        recycler.setLayoutManager(layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recycler.setEmptyView(emptyView);

        recycler.setAdapter(adapter = new PersonListAdapter(this, imageLoader, IMAGE_LOAD_TAG));
        if (savedInstanceState != null ) {
            adapter.loadState(savedInstanceState);
        }
        swipeRefresh.setOnRefreshListener(() -> {
            refreshData(true);
        });



        refreshData(false);


    }


    @Override
    protected void onStart() {
        super.onStart();

        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        eventBus.unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        adapter.saveState(bundle);
    }

    private void refreshData(boolean force) {
        subscribe(personController.getContacts(force)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(result -> {
                            setData(result);

                        },
                        throwable -> {
                            Timber.e("Error loading places!");

                            showSnackBar(R.id.coordinator, "Error loading places!");
                        }, () -> {
                            setRefreshing(false);
                        }));
    }

    public void setData(List<Person> persons) {
        adapter.setItems(persons);
    }

    @Subscribe
    public void onEvent(AlertDialogEvent event) {
        switch (event.requestId ) {
            case REQUEST_DELETE:
                if (event.button == AlertDialogEvent.Button.POSITIVE ) {
                    final int position = event.args.getInt(PARAM_POSITION);
                    doDeleteItem(position);
                }
                break;

            case REQUEST_SELECT_COLOR: {

                final int position = event.args.getInt(PARAM_POSITION);
                AlertDialogFragment.Item item = event.items.get(0);
                final int color = CELL_COLORS.get(item.id).color;
                doChageItemColor(position, color);

                break;
            }
        }
    }



    private void doDeleteItem(int position) {
        adapter.removeItem(position);
        personController.removeContact(position);
    }

    private void doChageItemColor(int position, int color) {
        adapter.setItemBackgroundColor(position, color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setRefreshing(boolean refreshing) {
        swipeRefresh.setRefreshing(refreshing);
    }


    @Override
    public void onPersonListItemClick(int position) {
        adapter.setItemExpanded(position, !adapter.isItemExpanded(position));
    }

    @Override
    public void onPersonListItemLongClick(int position) {
        Bundle params = new Bundle();
        params.putInt(PARAM_POSITION, position);

        final List<String> colorNames = Stream.of(CELL_COLORS).map(cellColor -> cellColor.colorName).collect(Collectors.toList());

        AlertDialogFragment.Item[] items = AlertDialogFragment.createItems(colorNames);
        AlertDialogFragment.show(getSupportFragmentManager(), REQUEST_SELECT_COLOR, "Select Color", null, null, null, null, null, items, false, params);

    }

    @Override
    public void onPersonListItemDeleteClick(int position) {
        Bundle params = new Bundle();
        params.putInt(PARAM_POSITION, position);
        AlertDialogFragment.show(getSupportFragmentManager(), REQUEST_DELETE, "Delete Item", "Do you want to delete this item?", "Yes", "No", null, null, null, false, params);
    }


}
