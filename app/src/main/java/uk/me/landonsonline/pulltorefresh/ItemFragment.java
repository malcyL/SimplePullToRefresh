package uk.me.landonsonline.pulltorefresh;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.refreshable.list.RefreshableListView;
import com.refreshable.list.RefreshableListView.OnListRefreshListener;
import com.refreshable.list.RefreshableListView.OnListLoadMoreListener;

import java.util.List;

import uk.me.landonsonline.pulltorefresh.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 */
public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener, OnListRefreshListener, OnListLoadMoreListener, LoaderCallbacks<DummyContent.DummyItem[]> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private RefreshableListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ItemArrayAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        mAdapter = new ItemArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        //RefreshableList lines start
        mListView = (RefreshableListView) view.findViewById(R.id.RefreshList);
        mListView.setOnListRefreshListener(this);//---------------------------------------------------------------Important
        mListView.setOnListLoadMoreListener(this);
        //RefreshableList Lines end

        getLoaderManager().initLoader(0, null, this);

        // Set the adapter
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        //mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    @Override
    public Loader<DummyContent.DummyItem[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<DummyContent.DummyItem[]>(getActivity())
        {
            @Override
            protected void onStartLoading()
            {
                forceLoad();
            }

            @Override
            public DummyContent.DummyItem[] loadInBackground()
            {
                try {
                    Thread.sleep(3000l);
                } catch (InterruptedException e) {
                }
                return DummyContent.getItemArray();
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<DummyContent.DummyItem[]> loader, DummyContent.DummyItem[] strings) {
        mAdapter.setData(strings);
        mListView.finishRefresh();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<DummyContent.DummyItem[]> loader) {
        mAdapter.clear();
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    public void Refresh(RefreshableListView list) {
        getLoaderManager().restartLoader(0, null, this);
    }

    public void LoadMore(RefreshableListView list) {
//        getLoaderManager().restartLoader(0, null, this);
    }

    public class ItemArrayAdapter extends ArrayAdapter<DummyContent.DummyItem> {

        public ItemArrayAdapter(Context context, int resource, int textViewResourceId, List<DummyContent.DummyItem> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public void setData(DummyContent.DummyItem[] data)
        {
            clear();
            if (data != null)
            {
                for(DummyContent.DummyItem i : data) {
                    add(i);
                }
            }
        }

    }
}
