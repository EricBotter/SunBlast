using UnityEngine;
using UnityEngine.SceneManagement;

public class MenuController : MonoBehaviour {

	// Use this for initialization
	void Start () {
		Screen.orientation = ScreenOrientation.LandscapeLeft;
		Screen.sleepTimeout = SleepTimeout.NeverSleep;
	}
	
	// Update is called once per frame
	void Update () {
		transform.Rotate(0, 1.5f*Time.deltaTime, 0);
	}

	public void PlayGameClick()
	{
		SceneManager.LoadScene("GameScene");
	}

	public void QuitClick()
	{
		Application.Quit();
	}
}
