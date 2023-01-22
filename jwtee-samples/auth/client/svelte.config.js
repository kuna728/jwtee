import adapter from '@sveltejs/adapter-static';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	kit: {
		adapter: adapter({
			fallback: "error.html"
		}),
		paths: {
			// assets: "/samples/auth",
			base: "/samples/auth"
		}
	}
};

export default config;
