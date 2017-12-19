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

	public int gameScore = 0;

	private List<Transform> activeSpheres = new List<Transform>();
	private List<Vector3> directions = new List<Vector3>();

	private static readonly Vector3 vector3 = new Vector3(3, 3, 3);

	private Texture2D[] planetTextures, texturesBumpMaps;
	
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
		planetTextures = Resources.LoadAll<Texture2D>("Planets");
		texturesBumpMaps = Resources.LoadAll<Texture2D>("PlanetsGrey");

		for (var i = 0; i < sphereCount; i++)
			AddSphere();
	}

	private void AddSphere()
	{
		var position = Random.onUnitSphere;
		position.Scale(vector3);
		var sphereTransform = Instantiate(spherePrefab, position, Quaternion.identity);
		var index = Random.Range(0, planetTextures.Length);
		var material = sphereTransform.GetComponent<Renderer>().material;
		material.mainTexture = planetTextures[index];
		material.shaderKeywords = new[]{"_NORMALMAP"};
		material.SetTexture("_BumpMap", texturesBumpMaps[index]);
		material.SetFloat("_BumpScale", 1.2f);
		sphereTransform.GetComponent<SphereController>().scale = Random.Range(0.15f, 0.5f);
		sphereTransform.GetComponent<SphereController>().speed = Random.Range(5f, 25f);
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
				var sphere = hit.collider.gameObject;
				gameScore += sphere.GetComponent<SphereController>().score;
				sphere.GetComponent<SphereController>().Explode();
				AddSphere();
			}
		}

		for (int i = 0; i < sphereCount; i++)
		{
			var sphere = activeSpheres[i];
			var direction = directions[i];
			sphere.RotateAround(Vector3.zero, direction, sphere.GetComponent<SphereController>().speed * Time.deltaTime);
		}
	}
}
