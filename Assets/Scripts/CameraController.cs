using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraController : MonoBehaviour {

	// Use this for initialization
	void Start ()
	{
		Screen.orientation = ScreenOrientation.LandscapeLeft;
		
		Input.gyro.enabled = true;
	}
	
	// Update is called once per frame
	void Update () {
		transform.Rotate (-Input.gyro.rotationRate.x, -Input.gyro.rotationRate.y, 0);
	}
}
