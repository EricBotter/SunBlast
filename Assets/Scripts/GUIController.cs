using System;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class GUIController : MonoBehaviour
{
    public Text counterText;
    public Text gameOverText;
    public Text scoreText;

    public Image crosshair;

    public Sprite greenReticle;
    public Sprite redReticle;

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
        scoreText.text = "Score: " + gameController.gameScore;
    }

    void UpdateCounter()
    {
        if (gameController.state != GameController.State.Running) return;
        counter -= Time.deltaTime;
        if (counter <= 0)
            GameOver();
        counterText.text = counter.ToString("0.0");
    }

    void GameOver()
    {
        gameOverText.text = "Game Over!";
        gameController.state = GameController.State.Ended;
        crosshair.sprite = redReticle;
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
                gameOverText.text = "Paused";
                crosshair.sprite = redReticle;
                break;
            case GameController.State.Paused:
                gameController.state = GameController.State.Running;
                crosshair.sprite = greenReticle;
                gameOverText.text = "";
                break;
            case GameController.State.Ended:
                SceneManager.LoadScene("MainMenu");
                break;
            default:
                throw new ArgumentOutOfRangeException();
        }
    }
}