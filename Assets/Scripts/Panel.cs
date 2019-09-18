using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Panel : MonoBehaviour
{
    public Vector2 range;
    public float sensitivity = 0.1f;
    private float x = 0.0f;
    
    void Start()
    {
        // by using a base sensitivity and then scaling, it will take the same amount of time to cross the screen at a certain
        // sensisitivity, no matter what width screen is used, making sensitivity universal
        float fullRange = range.y - range.x;
        float baseSensitivity = fullRange / Screen.width; // a sensitivity value normalized accross screen sizes
        sensitivity = baseSensitivity * 10.0f; // users modification to base sensitivity

        Cursor.lockState = CursorLockMode.Locked;
        Cursor.visible = false;
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKey(KeyCode.Escape))
        {
            Cursor.lockState = CursorLockMode.None;
            Cursor.visible = true;
        }

        transform.position = new Vector3(
            x, transform.position.y, transform.position.z
        );
    }

    void FixedUpdate()
    {
        x += Input.GetAxis("Mouse X") * sensitivity;
        x = Mathf.Clamp(x, range.x, range.y);
    }
}
