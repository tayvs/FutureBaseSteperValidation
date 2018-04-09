package futureValidator.useCase.validators

import futureValidator.Validator
import futureValidator.useCase.Campaign

import scala.concurrent.{ExecutionContext, Future}

object SecondValidator extends Validator[Campaign] {

  def isComplete(src: Campaign): Boolean = src.two.isDefined

  def validate(src: Campaign)(implicit ec: ExecutionContext): Future[Campaign] = Future {
    require(src.two.get equals "valid", "Two is not valid")
    Campaign(two = src.two)
  }

  def concat(acc: Campaign, value: Campaign): Campaign = acc.copy(two = value.two)


}
