package mobi.carton.modaltime.maze;


import android.os.Bundle;
import android.widget.GridView;

import mobi.carton.library.CartonActivity;
import mobi.carton.modaltime.R;

public class MazeActivity extends CartonActivity {



    private GridView gridViewMaze;


    /*
    LIFECYCLE
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        gridViewMaze = (GridView) findViewById(R.id.gridViewMaze);

        MazeAdapter adapter = new MazeAdapter(this);
        gridViewMaze.setAdapter(adapter);
    }
}
