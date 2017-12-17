using System.Collections;
using System.Collections.Generic;
using System.Security.Cryptography.X509Certificates;
using UnityEngine;

public class GameController : MonoBehaviour
{
	public Transform spherePrefab;
	public int sphereCount;

	public Camera mainCamera;

	public GUIController GuiController;

	private List<Transform> activeSpheres = new List<Transform>();
	private List<Vector3> directions = new List<Vector3>();	

	private static readonly Vector3 vector3 = new Vector3(3, 3, 3);
	
	// Use this for initialization
	void Start () {
		Debug.Log(sphereCount);
		for (var i = 0; i < sphereCount; i++)
		{
			Debug.Log("Adding "+i);
			AddSphere();
		}
	}

	private void AddSphere()
	{
		var position = Random.onUnitSphere;
		Debug.Log(position);
		position.Scale(vector3);
		Debug.Log(position);
		var gameObject = Instantiate(spherePrefab, position, Quaternion.identity);
		float scale_factor = Random.Range(0.05f,0.5f);
		gameObject.localScale = new Vector3(scale_factor, scale_factor , scale_factor);
		print(gameObject);
		activeSpheres.Add(gameObject);
		var direction = Random.onUnitSphere;
		directions.Add(direction);
	}

	// Update is called once per frame
	void Update () {
		Ray ray = mainCamera.ViewportPointToRay(new Vector3(0.5F, 0.5F, 0));
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
//			sphere.Translate(direction);
//			sphere.position.Normalize();
//			sphere.position.Scale(vector3);
		}
	}
}
