package futureValidator

sealed trait ValidationStopper

object ValidationStopper {

  case class ValidationStop[T](value: T) extends Exception() with ValidationStopper

  case class ValidationError[T](value: T, errorMsg: String) extends Exception(errorMsg) with ValidationStopper

  case object ValidationFailed extends Exception("Validation completely failed") with ValidationStopper

}
