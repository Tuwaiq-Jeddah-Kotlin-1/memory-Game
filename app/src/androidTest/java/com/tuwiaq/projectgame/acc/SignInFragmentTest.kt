package com.tuwiaq.projectgame.acc



import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


class SignInFragmentTest {

    private lateinit var signInFragment: SignInFragment
    @Before
    fun boo()  {

           signInFragment = SignInFragment()
    }
      @Test
    fun checkEmail(){
        val result = signInFragment.boo("sweetlotus@gmail.com","123456")
          val result2= signInFragment.boo("sweetlotus@gmail.com","123456A")
          assert(result2)

  /*
          val result2 = signInFragment.boo("sff@gmail.com","1")
          assertTrue(result2)*/
       //  assertEquals(result,true)

          assertTrue(result)


      }
}