package Utils.State;

import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;

public class MySymTblStack extends MyStack<MyIDic<String, Value>> {
//    public MySymTblStack deepCopy() {
//        MySymTblStack newStack = new MyException();
//        MyStack<MyIDictionary<String, Value>> tempStack = new MyStack<>();
//
//        while (!this.stack.empty())
//            tempStack.push(this.stack.pop());
//
//        while (!tempStack.isEmpty()) {
//            stack.push(tempStack.peek());
//            try {
//                newStack.push(tempStack.pop().deepCopy());
//            } catch (InterpreterException e) {
//                throw new InterpreterException(e.getMessage());
//            }
//        }
//
//        return newStack;
//    }
}