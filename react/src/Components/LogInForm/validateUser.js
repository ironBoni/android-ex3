import { users } from "../../Data/data";

export default function validateInfo(values) {
  var isValidateOk = true;
  let errors = {};

  //user field is empty
  if (!values.id.trim()) {
    errors.id = 'id is required';
    isValidateOk = false;
    return { errors, isValidateOk };
  }
  
  return { errors, isValidateOk };
}