using System;
using UnityEngine;
using UnityEngine.UI;

public class GUIController : MonoBehaviour
{
    public Text counter_text;
    private double counter;
    private double secondsToAdd;

    // Use this for initialization
    void Start()
    {
        counter = 10;
    }

    // Update is called once per frame
    void Update()
    {
        UpdateCounter();
    }

    void UpdateCounter()
    {
        counter -= Time.deltaTime;
        print(Time.deltaTime);
        counter_text.text = counter <= 0 ? "Game Over" : counter.ToString();
    }

    public void IncreaseCounter()
    {
        counter += secondsToAdd;
        secondsToAdd = Math.Max(0.3, secondsToAdd * 0.9);
    }
}