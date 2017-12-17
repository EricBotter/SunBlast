using System.Collections;
using System.Collections.Generic;
using System.Security.Cryptography.X509Certificates;
using UnityEngine;

public class GameController : MonoBehaviour
{
	public Transform spherePrefab;
	public int sphereCount;

	public Camera mainCamera;
	public Light cameraSpotlight;

	public GUIController GuiController;

	public Texture2D[] textures;

	private List<Transform> activeSpheres = new List<Transform>();
	private List<Vector3> directions = new List<Vector3>();	

	private static readonly Vector3 vector3 = new Vector3(3, 3, 3);
	
	public enum State
	{
		Running, Paused, Ended
	}

	private State _state;
	public State state
	{
		get { return _state; }
		set
		{
			_state = value;
			cameraSpotlight.intensity = _state == State.Running ? 3 : 0;
		}
	}
	
	// Use this for initialization
	void Start () {
		for (var i = 0; i < sphereCount; i++)
		{
			Debug.Log("Adding "+i);
			AddSphere();
		}
	}

	private void AddSphere()
	{
		var position = Random.onUnitSphere;
		position.Scale(vector3);
		var sphereTransform = Instantiate(spherePrefab, position, Quaternion.identity);
		var scaleFactor = Random.Range(0.05f,0.5f);
		sphereTransform.GetComponent<Renderer>().material.mainTexture = textures[Random.Range (0, textures.Length)];
		sphereTransform.localScale = new Vector3(scaleFactor, scaleFactor , scaleFactor);
		print(sphereTransform);
		activeSpheres.Add(sphereTransform);
		var direction = Random.onUnitSphere;
		directions.Add(direction);
	}

	// Update is called once per frame
	void Update () {
		if (state != State.Running) return;
		
		var ray = mainCamera.ViewportPointToRay(new Vector3(0.5F, 0.5F, 0));
		RaycastHit hit;
		if (Physics.Raycast(ray, out hit)) {
			print("Target hit! " + hit.transform.name);
			var index = activeSpheres.IndexOf(hit.transform);
			if (index < 0)
				Debug.Log("rip");
			else
			{
				GuiController.IncreaseCounter();
				activeSpheres.RemoveAt(index);
				directions.RemoveAt(index);
				DestroyImmediate(hit.collider.gameObject);
				AddSphere();
			}
		}

		for (int i = 0; i < sphereCount; i++)
		{
			var sphere = activeSpheres[i];
			var direction = directions[i];
			sphere.RotateAround(Vector3.zero, direction, 5 * Time.deltaTime);
		}
	}
}
