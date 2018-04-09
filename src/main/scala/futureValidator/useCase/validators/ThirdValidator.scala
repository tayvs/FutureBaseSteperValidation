package futureValidator.useCase.validators

import futureValidator.Validator
import futureValidator.useCase.Campaign

import scala.concurrent.{ExecutionContext, Future}

object ThirdValidator extends Validator[Campaign] {

  def isComplete(src: Campaign): Boolean = src.three.isDefined

  def validate(src: Campaign)(implicit ec: ExecutionContext): Future[Campaign] = Future {
    require(src.three.get equals "valid", "Three is not valid")
    Campaign(three = src.three)
  }

  def concat(acc: Campaign, value: Campaign): Campaign = acc.copy(three = value.three)


}
