package futureValidator.useCase.validators

import futureValidator.Validator
import futureValidator.useCase.Campaign

import scala.concurrent.{ExecutionContext, Future}

object OneValidator extends Validator[Campaign] {

  def isComplete(src: Campaign): Boolean = src.one.isDefined

  def validate(src: Campaign)(implicit ec: ExecutionContext): Future[Campaign] = Future {
    require(src.one.get equals "valid", "One is not valid")
    Campaign(one = src.one)
  }

  def concat(acc: Campaign, value: Campaign): Campaign = acc.copy(one = value.one)


}
