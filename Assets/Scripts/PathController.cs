using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class PathController : MonoBehaviour
{
    private static float start;

    public float speed = 2.0f;
    public float noteStep = 0.5f;

    public static void Set()
    {
        start = Time.timeSinceLevelLoad;
    }
    void Update()
    {
        float now = Time.timeSinceLevelLoad;
        float elapsed = now - start;
        float beatsPassed = elapsed / GameController.beat;
        transform.position = new Vector3(
            0.0f, 0.0f, beatsPassed * noteStep * speed
        );
    }
}
