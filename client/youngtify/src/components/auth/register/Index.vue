<template>
  <div class="intro-y box px-5 py-8 mt-4">
    <div class="intro-y">
      <input
        type="text"
        class="intro-y auth__input input input--lg w-full border block"
        placeholder="First Name"
      />
      <input
        type="text"
        class="intro-y auth__input input input--lg w-full border block mt-4"
        placeholder="Last Name"
      />
      <input
        type="text"
        class="intro-y auth__input input input--lg w-full border block mt-4"
        placeholder="Email"
      />
      <input
        type="password"
        class="intro-y auth__input input input--lg w-full border block mt-4"
        placeholder="Password"
      />
      <div class="intro-y w-full grid grid-cols-12 gap-4 h-1 mt-3">
        <div
          class="auth__password-strength auth__password-strength--red col-span-3 h-full rounded"
        ></div>
        <div
          class="auth__password-strength auth__password-strength--red col-span-3 h-full rounded"
        ></div>
        <div
          class="auth__password-strength auth__password-strength--red col-span-3 h-full rounded"
        ></div>
        <div class="auth__password-strength col-span-3 h-full rounded"></div>
      </div>
      <a href="" class="intro-y auth__info block mt-2 text-xs sm:text-sm -mb-1"
        >What is a secure password?</a
      >
      <input
        type="password"
        class="intro-y auth__input input input--lg w-full border block mt-4"
        placeholder="Password Confirmation"
      />
    </div>
    <div class="intro-y flex items-center auth__info mt-4 text-xs sm:text-sm">
      <input type="checkbox" class="input border mr-2" id="remember-me" />
      <label class="cursor-pointer select-none" for="remember-me"
        >I agree to the Envato</label
      >
      <a class="ml-1 underline" href="">Privacy Policy</a>.
    </div>
    <div class="intro-y mt-5 xl:mt-8 text-center xl:text-left">
      <button class="intro-y button button--lg button--primary w-full xl:mr-3">
        Register
      </button>
      <button
        class="intro-y button button--lg button--secondary w-full border mt-3"
      >
        Sign in
      </button>
    </div>
  </div>
</template>

<script>
export default {
    name: 'Register'
};
</script>

<style>
</style>