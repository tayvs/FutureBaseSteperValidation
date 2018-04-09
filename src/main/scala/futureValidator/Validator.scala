package futureValidator

import futureValidator.ValidationStopper.ValidationError

import scala.concurrent.{ExecutionContext, Future}

trait Validator[T] {

  @throws[ValidationError[T]]
  def validate(src: T)(implicit ec: ExecutionContext): Future[T]
  def concat(acc: T, value: T): T
  def isComplete(src: T): Boolean


}
