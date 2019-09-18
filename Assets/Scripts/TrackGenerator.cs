using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TrackGenerator : MonoBehaviour
{
    public PathController pControl;

    public GameObject[] note;

    public float offset = 0;
    public float step = 0.25f;

    public static int[] track = new int[]
    {
        1, 0, 0, 0, 0, 0, 0, 0,
        1, 0, 0, 0, 0, 0, 0, 0,
        1, 0, 0, 0, 0, 0, 0, 0,
        1, 0, 0, 0, 0, 0, 0, 0,

        1, 0, 0, 0, 1, 0, 0, 0,
        1, 0, 0, 0, 1, 0, 0, 0,
        1, 0, 0, 0, 1, 0, 0, 0,
        1, 0, 0, 0, 1, 0, 0, 0,

        1, 0, 1, 0, 1, 0, 1, 0,
        1, 0, 1, 0, 1, 1, 1, 0,
        1, 0, 1, 0, 1, 0, 1, 0,
        1, 0, 1, 0, 1, 1, 1, 0,

        1, 0, 1, 0, 1, 0, 1, 0,
        1, 0, 1, 0, 0, 1, 1, 1,
        1, 0, 1, 0, 1, 0, 1, 0,
        1, 0, 1, 0, 1, 1, 1, 0
    };
    
    void Start()
    {
        for(int i = 0; i < track.Length; i++)
        {
            if (track[i] == 0) continue;

            Instantiate(
                note[track[i] - 1],
                new Vector3(0, 0, (offset + (i * step)) * pControl.speed),
                Quaternion.identity,
                gameObject.transform
            );
        }
    }
}
