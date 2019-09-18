using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GameController : MonoBehaviour
{
    // 128 bpm
    public static float beat = 60.0f / 128.0f;

    public Text countdownText;

    public AudioSource music;

    public float songStart;

    void Start()
    {
        music.time = songStart;
        StartCoroutine(Countdown());
    }

    IEnumerator Countdown()
    {
        yield return new WaitForSeconds(beat);
        countdownText.text = "3";
        yield return new WaitForSeconds(beat);
        countdownText.text = "2";
        yield return new WaitForSeconds(beat);
        countdownText.text = "1";
        yield return new WaitForSeconds(beat);
        PathController.Set();
        music.Play();
        countdownText.text = "GO!";
        yield return new WaitForSeconds(beat);
        countdownText.text = "";
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
