import 'package:flutter/material.dart';
import 'package:frontend/userapiauth.dart';
import 'package:frontend/register_page.dart';
import 'package:frontend/themes/app_colors.dart';
import 'package:flutter_svg/flutter_svg.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({Key? key}) : super(key: key);

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  bool _isLoading = false;

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  Future<void> _login() async {
    // validasi formnya
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = true;
      });

      // buat model user input
      try {
        final user = UserModel(
          username: _usernameController.text.trim(),
          password: _passwordController.text,
        );

        final response = await UserApiService.loginUser(user);

        if (response.errorCode == "200") {
          //jika login berhasil
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Login Successful!'),
              backgroundColor: Colors.green,
            ),
          );
        } else if (response.errorCode == "204" && response.errorMessage == "User Not Found"){
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('User Not Found!'),
              backgroundColor: Colors.red,
            ),
          );
        } else if (response.errorCode == "204" && response.errorMessage == "Wrong Password") {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Wrong Password!'),
              backgroundColor: Colors.red,
            ),
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Login Failed: ${response.errorMessage}'),
              backgroundColor: Colors.red,
            ),
          );
        }
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error: $e'),
            backgroundColor: Colors.red,
          ),
        );
      } finally {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
       body: Column(
        children: [
          _buildHeader(),
          // Bungkus widget form card dengan Expanded
          Expanded(
            child: Container(
              color: AppColors.background, 
              child: Transform.translate(
                offset: const Offset(0, -50),
                child: _buildFormCard(),
              ),
            ),
          ),
        ],
      ),
    );
  }

  /// Widget untuk header biru melengkung di bagian atas
  Widget _buildHeader() {
    return ClipPath(
      // clipper: _HeaderClipper(),
      child: Container(
        height: 200,
        color: AppColors.primary,
        child: const Center(
          child: Text(
            'FOCUSREALM',
            style: TextStyle(
              fontFamily: 'Poppins', // Ganti dengan font Anda
              color: AppColors.background,
              fontSize: 36,
              fontWeight: FontWeight.bold,
              letterSpacing: 2,
            ),
          ),
        ),
      ),
    );
  }

  /// Widget untuk kartu yang berisi semua elemen form
  Widget _buildFormCard() {
    return Container(
      decoration: BoxDecoration(
        color: AppColors.background,
        // Ubah borderRadius agar hanya dua sudut atas yang melengkung
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(40),
          topRight: Radius.circular(40),
        ),
      ),
      child: SingleChildScrollView(

        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 32),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                'Login',
                style: TextStyle(
                  color: AppColors.primary,
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 24),
              _buildTextField(
                controller: _usernameController,
                hint: 'Username',
                icon: Icons.person_outline,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Username is required";
                  }
                  if (value.length < 3) {
                    return 'Username must be at least 3 characters';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),
              _buildTextField(
                controller: _passwordController,
                hint: 'Password',
                icon: Icons.lock_outline,
                isPassword: true,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Password is required";
                  }
                  if (value.length < 6) {
                    return 'Password must be at least 6 characters';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 32),
              _buildSubmitButton(),
              const SizedBox(height: 24),
              _buildSocialLoginDivider(),
              const SizedBox(height: 24),
              _buildSocialLoginButtons(),
              const SizedBox(height: 32),
              _buildLoginRedirect(),
            ],
          ),
        ),
      ),
    );
  }  

  /// Widget modular untuk text field
  Widget _buildTextField({
    required TextEditingController controller,
    required String hint,
    required IconData icon,
    bool isPassword = false,
    TextInputType? keyboardType,
    String? Function(String?)? validator,
  }) {
    return TextFormField(
      controller: controller,
      obscureText: isPassword,
      keyboardType: keyboardType,
      decoration: InputDecoration(
        hintText: hint,
        prefixIcon: Icon(icon, color: AppColors.light),
        filled: true,
        fillColor: AppColors.background,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: const BorderSide(color: AppColors.primary),
        ),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: const BorderSide(color: Colors.red),
        ),
      ),
      validator: validator,
    );
  }
  
  /// Widget untuk tombol Sign Up dengan gradien
  Widget _buildSubmitButton() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [AppColors.primary, AppColors.secondary],
          begin: Alignment.centerLeft,
          end: Alignment.centerRight,
        ),
        borderRadius: BorderRadius.circular(12),
      ),
      child: ElevatedButton(
        onPressed: _isLoading ? null : _login,
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.transparent,
          shadowColor: Colors.transparent,
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
        child: _isLoading
            ? const SizedBox(
                height: 24,
                width: 24,
                child: CircularProgressIndicator(color: AppColors.background, strokeWidth: 3),
              )
            : const Text(
                'Login',
                style: TextStyle(
                  color: AppColors.background,
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
      ),
    );
  }

  /// Widget untuk pemisah "Or sign up with"
  Widget _buildSocialLoginDivider() {
    return const Row(
      children: [
        Expanded(child: Divider(color: AppColors.light)),
        Padding(
          padding: EdgeInsets.symmetric(horizontal: 8.0),
          child: Text(
            'Or Login with',
            style: TextStyle(color: AppColors.light),
          ),
        ),
        Expanded(child: Divider(color: AppColors.light)),
      ],
    );
  }

  /// Widget untuk tombol-tombol social login
  Widget _buildSocialLoginButtons() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        _socialButton('assets/images/google-icon.svg'),
        const SizedBox(width: 20),
        _socialButton('assets/images/facebook-icon.svg'),
        const SizedBox(width: 20),
        _socialButton('assets/images/x-icon.svg'),
      ],
    );
  }
  
  Widget _socialButton(String assetPath) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: AppColors.background,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.grey.shade300),
      ),
      child: SvgPicture.asset(
        assetPath,
        height: 24,
        width: 24,
      ),
    );
  }

  /// Widget untuk teks dan tombol navigasi ke halaman Login
  Widget _buildLoginRedirect() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Text(
          "Don't Have An Account? ",
          style: TextStyle(color: AppColors.light),
        ),
        GestureDetector(
          onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => RegisterPage()),
                      );
                    },
          child: const Text(
            'Sign Up',
            style: TextStyle(
              color: AppColors.primary,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ],
    );
  }
}