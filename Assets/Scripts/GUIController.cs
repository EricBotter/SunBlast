﻿using System;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class GUIController : MonoBehaviour
{
    public Text counter_text;

    public GameController gameController;
    
    private double counter;
    private double secondsToAdd = 5;
    
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
        if (gameController.state != GameController.State.Running) return;
        counter -= Time.deltaTime;
        if (counter <= 0)
        {
            counter_text.text = "Game Over!";
            gameController.state = GameController.State.Ended;
            GameOver();
        }
        else
            counter_text.text = counter.ToString("0.0");
    }

    void GameOver()
    {
        
    }

    public void IncreaseCounter()
    {
        counter += secondsToAdd;
        secondsToAdd = Math.Max(0.3, secondsToAdd * 0.9);
    }

    public void PauseButtonClick()
    {
        switch (gameController.state)
        {
            case GameController.State.Running:
                gameController.state = GameController.State.Paused;
                break;
            case GameController.State.Paused:
                gameController.state = GameController.State.Running;
                break;
            case GameController.State.Ended:
                SceneManager.LoadScene("MainMenu");
                break;
        }
    }
}