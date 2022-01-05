package com.tuwiaq.projectgame.acc



import org.junit.After
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

  /*
          val result2 = signInFragment.boo("sff@gmail.com","1")
          assertTrue(result2)*/
          assert(result)

         // assertTrue(result)


      }
}