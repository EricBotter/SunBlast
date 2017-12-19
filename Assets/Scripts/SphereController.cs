using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SphereController : MonoBehaviour
{
    private int _score;
    private float _scale;

    public float scale
    {
        get { return _scale; }
        set
        {
            _scale = value;
            transform.localScale = new Vector3(_scale, _scale, _scale);
            if (_scale < 0.2)
            {
                _score = 3;
            }
            else if (_scale < 0.4)
            {
                _score = 2;
            }
            else
            {
                _score = 1;
            }
        }
    }
    
    public int score
    {
        get { return _score; }
    }

    // Use this for initialization
    void Start()
    {
    }

    // Update is called once per frame
    void Update()
    {
    }

    public void Explode()
    {
        var mesh = GetComponent<MeshRenderer>();
        mesh.enabled = false;
        var exp = GetComponent<ParticleSystem>();
        exp.Play();
        Destroy(gameObject, exp.duration);
    }
}