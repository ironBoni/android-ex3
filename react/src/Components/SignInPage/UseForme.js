import { useState, useEffect, useRef } from 'react';
import { dataServer } from '../../Data/data';

const useForm = (submitForm, validate, type, setUsername, setToken) => {
  const [values, setValues] = useState({
    id: '',
    name: '',
    profileImage: '/images/default.jpg',
    password: '',
    confPassword: '',
  });
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoginOk, setIsLoginOk] = useState(false);

  var users = [];

  useEffect(async () => {
    var response = await fetch(dataServer + "api/Register");
    var usersList = await response.json();
    users = usersList.fulLList;
  });

  var correctPass = useRef("");
  var oldUser = useRef("");

  const handleChange = e => {
    const { name, value } = e.target;
    if (name === "image-upload") {
      upImge(e)
    } else {
      setValues({
        ...values,
        [name]: value
      });
    }

  };
  const upImge = (e) => {
    let file = e.target.files[0];

    var fileReader = new FileReader();
    fileReader.readAsDataURL(file);

    fileReader.onload = () => {
      var profileBase64 = fileReader.result;

      values.profileImage = profileBase64

    };
  }

  function handleLogin(e) {
    setUsername(values.id);
    var result = validate(values)
    setErrors(result.errors);
    if (values.password === correctPass.current && values.id === oldUser) {
      submitForm();
      return;
    }


    if (Object.keys(result.errors).length === 0) {
      setUsername(values.id);
      waitForToken();
    }
  };


  async function handleSubmit(e) {
    e.preventDefault();
    var result = validate(values, users)
    setErrors(result.errors);
    if (result.flag) {
      users.push({
        id: values.id, name: values.name, password: values.password,
        profileImage: values.profileImage
      })

      // POST request to add contact to server
      var data = {
        "id": values.id, "name": values.name, "password": values.password,
        "profileImage": values.profileImage, "server": "http://localhost:5186"
      };
      var config = {
        method: 'POST',
        headers: {
          'accept': '*/*',
          'content-Type': 'application/json',
        },
        body: JSON.stringify(data)
      }
      var res = await fetch(dataServer + "api/Register", config);

      setValues({
        id: '',
        name: '',
        profileImage: '/images/default.jpg',
        password: '',
        confPassword: '',

      })
      submitForm();
    }
  };

  function waitForToken() {
    fetch(dataServer + "api/Login", {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Accept-Endcoding': 'gzip, deflate, br',
        'Connection': 'keep-alive',
        'Content-type': 'application/json',
      },
      body: JSON.stringify({ "username": values.id, "password": values.password })
    })
      .then(res => res.json()).then(loginResponse => {
        correctPass.current = loginResponse.correctPass;
        correctPass.current = values.id;
        if (loginResponse.token)
          setToken(loginResponse.token.token);
        if (loginResponse.isCorrectInput === true) {
          submitForm();
        }
        else {
          var notOkLoginErrors = { password: "Id or Password are incorrect" };
          setErrors(notOkLoginErrors);
          return;
        }
      });
  }

  if (type == 'login')
    return { handleChange, handleLogin, values, errors };
  return { handleChange, handleSubmit, values, errors };
};

export default useForm;